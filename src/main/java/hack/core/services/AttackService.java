package hack.core.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import hack.core.actor.config.ActorConfig;
import hack.core.actor.messages.AttackMessage;
import hack.core.dto.APIResultDTO;
import hack.core.dto.APIResultType;
import hack.core.models.AttackLog;
import hack.core.models.AttackType;
import hack.core.models.BattleReport;
import hack.core.models.BattleReportWinner;
import hack.core.models.Location;
import hack.core.models.Player;
import hack.core.models.ResearchType;
import hack.core.models.TransitTroop;
import hack.core.models.Troop;
import hack.core.models.TroopType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import scala.concurrent.util.Duration;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;

@Service
public class AttackService {

	@Autowired
	private PlayerService playerService;
	@Autowired
	private LocationService locationService;
	@Autowired
	private BattleReportService battleReportService;

	@Autowired
	@Qualifier(ActorConfig.ATTACK_ACTOR)
	private ActorRef attackActor;
	@Autowired
	private ActorSystem actorSystem;

	public APIResultDTO stealMoneyAttack(Player sourcePlayer, String targetIp) {

		// Validate IP
		Player targetPlayer = playerService.getPlayerByLocationIP(targetIp);
		if (targetPlayer == null) {
			return new APIResultDTO(APIResultType.ERROR, "Unknown IP: " + targetIp);
		}

		APIResultType result = APIResultType.WARNING;
		String message = "Error, something went wrong";
		boolean success = false;
		// Have I attacked recently?
		if (sourcePlayer.getRecentAttackForIp(targetIp) != null) {
			return new APIResultDTO(APIResultType.WARNING, "You have recently attacked: " + targetIp);
		}

		// Who won?
		int attack = sourcePlayer.researchOfType(ResearchType.MONEY_ATTACK).getLevel();
		int defense = targetPlayer.researchOfType(ResearchType.MONEY_DEFENSE).getLevel();

		if (attack > defense) {
			int amount = (int) Math.ceil(targetPlayer.getMoney() / 100.0 * 35.0); // 35%
			sourcePlayer.setMoney(sourcePlayer.getMoney() + amount);
			targetPlayer.setMoney(targetPlayer.getMoney() - amount);
			result = APIResultType.SUCCESS;
			success = true;
			message = "Gained £" + amount;
		} else {
			message = "Attack failed";
		}
		AttackLog attackLog = new AttackLog(AttackType.STEAL_MONEY, new Date(), sourcePlayer.getName(), targetIp, success, message);

		// TODO - Need to validate that this method of ensuring only recent
		// dates are here works / is performant
		Date cutoff = new Date(new Date().getTime() - (60 * 60 * 1000));
		sourcePlayer.getLogs().setStealMoneyAttackCooldown(
				sourcePlayer.getLogs().getStealMoneyAttackCooldown().stream().filter(a -> a.getTime().after(cutoff)).collect(Collectors.toList()));
		sourcePlayer.getLogs().getStealMoneyAttackCooldown().add(attackLog);
		playerService.save(sourcePlayer);
		targetPlayer.getLogs().setStealMoneyAttackCooldown(
				targetPlayer.getLogs().getStealMoneyAttackCooldown().stream().filter(a -> a.getTime().after(cutoff)).collect(Collectors.toList()));
		targetPlayer.getLogs().getStealMoneyAttackCooldown().add(attackLog);
		playerService.save(targetPlayer);

		return new APIResultDTO(result, message);
	}

	public APIResultDTO takeoverAttack(Player sourcePlayer, String sourceIp, String targetIp, List<Troop> desiredTroops, int ceo) {

		// Validate IP
		Player targetPlayer = playerService.getPlayerByLocationIP(targetIp);
		if (targetPlayer == null) {
			return new APIResultDTO(APIResultType.ERROR, "Unknown IP: " + targetIp);
		}

		// Validate source
		Location source = locationService.getLocationFromIp(sourceIp);
		if (source == null) {
			return new APIResultDTO(APIResultType.ERROR, "Source IP not valid: " + sourceIp);
		}

		// Validate target
		Location target = locationService.getLocationFromIp(targetIp);
		if (target == null) {
			return new APIResultDTO(APIResultType.ERROR, "Target IP not valid: " + targetIp);
		}

		int ceosAvailable = sourcePlayer.getCeoCount();
		if (ceosAvailable < ceo) {
			return new APIResultDTO(APIResultType.ERROR, "You do not have enough CEOs");
		}

		APIResultType result = APIResultType.WARNING;
		String message = "Error, something went wrong";

		// Am I sending an attack already?!
		if (sourcePlayer.getRecentAttackForIp(targetIp) != null) {
			return new APIResultDTO(APIResultType.WARNING, "You have recently attacked: " + targetIp);
		}

		// Calculate attack time
		Date startTime = new Date();
		long transitTimeInSeconds = locationService.calculateAttackTransitTime(source, target);
		transitTimeInSeconds = 30; // TODO - Remove this when testing is
									// finished
		Date arrivalTime = new Date(startTime.getTime() + (transitTimeInSeconds * 1000));

		// Have I got enough troops?
		List<Troop> remainingTroops = source.getDefense();
		List<TransitTroop> attackingTroops = new ArrayList<TransitTroop>();

		boolean enoughTroops = true;
		for (Troop desiredTroop : desiredTroops) {
			for (Troop remainingTroop : remainingTroops) {
				if (desiredTroop.getType().equals(remainingTroop.getType())) {
					// Check that there is enough
					// decrement remaining
					remainingTroop.setNoOfTroops(remainingTroop.getNoOfTroops() - desiredTroop.getNoOfTroops());
					attackingTroops.add(new TransitTroop(desiredTroop.getType(), desiredTroop.getNoOfTroops(), sourceIp, targetIp, arrivalTime));
					if (remainingTroop.getNoOfTroops() < 0) {
						enoughTroops = false;
					}
					break;
				}
			}
		}
		if (enoughTroops) {
			source.setDefense(remainingTroops);
			source.getAttackTransitOut().addAll(attackingTroops);
			target.getAttackTransitIn().addAll(attackingTroops);

			result = APIResultType.SUCCESS;
			message = "Attack sent - Will be arrive: " + arrivalTime;
		} else {
			return new APIResultDTO(APIResultType.WARNING, "You do not have enough troops");
		}

		AttackMessage attackMessage = new AttackMessage(sourcePlayer.getId(), targetPlayer.getId(), attackingTroops, ceo);
		actorSystem.scheduler().scheduleOnce(Duration.create(transitTimeInSeconds, TimeUnit.SECONDS), attackActor, attackMessage);

		playerService.save(sourcePlayer);
		playerService.save(targetPlayer);
		locationService.save(source);
		locationService.save(target);

		return new APIResultDTO(result, message);
	}

	public APIResultDTO scan(Player source, String targetIp) {
		APIResultType result = APIResultType.WARNING;
		String message = "Error, something went wrong";

		Player targetPlayer = playerService.getPlayerByLocationIP(targetIp);
		if (targetPlayer == null) {
			return new APIResultDTO(APIResultType.ERROR, "Unknown IP: " + targetIp);
		}

		// Who won?
		int attack = source.researchOfType(ResearchType.SCAN_POWER).getLevel();
		int defense = targetPlayer.researchOfType(ResearchType.SCAN_POWER).getLevel();

		if (attack > defense) {
			result = APIResultType.SUCCESS;
			message = "Scan successful - " + targetPlayer.getName() + " - Defense level: "
					+ targetPlayer.researchOfType(ResearchType.MONEY_DEFENSE).getLevel(); // TODO
																							// -
																							// Add
																							// proper
																							// details
		} else {
			message = "Scan failed";
		}
		return new APIResultDTO(result, message);
	}

	public void completeTakeoverAttack(AttackMessage attackMessage) {
		System.out.println(attackMessage);

		Player sourcePlayer = playerService.getPlayerById(attackMessage.getSourcePlayerId());
		Location source = locationService.getLocationFromIp(attackMessage.getAttackingTroops().get(0).getSource());
		List<TransitTroop> attackingTroops = attackMessage.getAttackingTroops();
		source.getAttackTransitOut().removeAll(attackingTroops);
		List<TransitTroop> attackingKilled = new ArrayList<TransitTroop>();
		List<TransitTroop> attackingRemaining = new ArrayList<TransitTroop>();

		Player targetPlayer = playerService.getPlayerById(attackMessage.getTargetPlayerId());
		Location target = locationService.getLocationFromIp(attackMessage.getAttackingTroops().get(0).getTarget());
		target.getAttackTransitIn().removeAll(attackingTroops);
		List<Troop> defendingTroops = target.getDefense();
		List<Troop> defendingKilled = new ArrayList<Troop>();
		List<Troop> defendingRemaining = new ArrayList<Troop>();
		List<TransitTroop> defendingInTroops = target.getDefenseIn();
		List<TransitTroop> defendingInKilled = new ArrayList<TransitTroop>();
		List<TransitTroop> defendingInRemaining = new ArrayList<TransitTroop>();

		double dBase = targetPlayer.researchOfType(ResearchType.DEFENCE_BASE).getLevel() * 1.1;

		int attackVictory = 0;
		int defenseVictory = 0;

		for (TroopType type : TroopType.values()) {

			double factor;
			long remainingAttacking, remainingDefending;

			TransitTroop attackingTroop = getTransitTroopOfType(type, attackingTroops);
			Troop defendingTroop = getTroopsOfType(type, defendingTroops);
			List<TransitTroop> defendingInTroopsOfType = getTransitTroopsOfType(type, defendingInTroops);

			double aBonus = sourcePlayer.researchOfType(ResearchType.valueOf("ATTACK_BONUS_" + type.toString())).getLevel() * 1.1;
			double aScore = attackingTroop.getNoOfTroops() * aBonus * randomAdjustment();

			double dBonus = targetPlayer.researchOfType(ResearchType.valueOf("DEFENSE_HARDEN_" + type.toString())).getLevel() * 1.1;
			double dScoreTroopCountOwn = defendingTroop.getNoOfTroops();
			double dScoreTroopCountIn = defendingInTroopsOfType.stream().mapToDouble(t -> t.getNoOfTroops()).sum();
			double dScore = ((dScoreTroopCountOwn + dScoreTroopCountIn * dBonus) + (dBase * 5)) * randomAdjustment();

			System.out.println("Attack on " + target.getIp() + " - " + type + " - attack: " + aScore + " defense: " + dScore);
			if (aScore > dScore) {
				factor = Math.abs(aScore - dScore) / Math.abs(aScore);

				remainingAttacking = (long) (attackingTroop.getNoOfTroops() * factor);
				attackingRemaining.add(new TransitTroop(type, remainingAttacking, source.getIp(), target.getIp(), attackingTroop.getArrival()));
				attackingKilled.add(new TransitTroop(type, attackingTroop.getNoOfTroops() - remainingAttacking, source.getIp(), target.getIp(), attackingTroop.getArrival()));

				defendingRemaining.add(new Troop(type, 0));
				defendingKilled.add(new Troop(type, defendingTroop.getNoOfTroops()));

				for (TransitTroop defendingInTroop : defendingInTroopsOfType) {
					defendingInRemaining.add(new TransitTroop(defendingInTroop.getType(), 0, defendingInTroop.getSource(), defendingInTroop
							.getTarget(), defendingInTroop.getArrival()));
					defendingInKilled.add(new TransitTroop(defendingInTroop.getType(), defendingInTroop.getNoOfTroops(),
							defendingInTroop.getSource(), defendingInTroop.getTarget(), defendingInTroop.getArrival()));
				}

				attackVictory++;
			} else {
				factor = Math.abs(dScore - aScore) / Math.abs(dScore);

				attackingRemaining.add(new TransitTroop(type, 0, source.getIp(), target.getIp(), attackingTroop.getArrival()));
				attackingKilled.add(new TransitTroop(type, attackingTroop.getNoOfTroops(), source.getIp(), target.getIp(), attackingTroop.getArrival()));

				remainingDefending = (long) (defendingTroop.getNoOfTroops() * factor);
				defendingRemaining.add(new Troop(type, remainingDefending));
				defendingKilled.add(new Troop(type, defendingTroop.getNoOfTroops() - remainingDefending));

				for (TransitTroop defendingInTroop : defendingInTroopsOfType) {
					long remain = (long) (defendingTroop.getNoOfTroops() * factor);
					defendingInRemaining.add(new TransitTroop(defendingInTroop.getType(), remain, defendingInTroop.getSource(), defendingInTroop
							.getTarget(), defendingInTroop.getArrival()));
					defendingInKilled.add(new TransitTroop(defendingInTroop.getType(), defendingInTroop.getNoOfTroops() - remain, defendingInTroop
							.getSource(), defendingInTroop.getTarget(), defendingInTroop.getArrival()));
				}

				defenseVictory++;
			}
		}
		Date now = new Date();

		BattleReportWinner winner;
		boolean takeoverComplete = false;
		String message;

		if (attackVictory > defenseVictory) {
			System.out.println("Attack success");
			target.setPlayer(sourcePlayer.getId());
			List<Troop> newDefence = new ArrayList<Troop>();
			for (TransitTroop troop : attackingRemaining) {
				newDefence.add(new Troop(troop.getType(), troop.getNoOfTroops()));
			}
			target.setDefense(newDefence); //TODO - Need to do something about returning troops etc
			target.setNpc(false);
			target.setResearchHideLevel(source.getResearchHideLevel());
			sourcePlayer.getLocationIps().add(target.getIp());
			targetPlayer.getLocationIps().remove(target.getIp());
			winner = BattleReportWinner.ATTACK;

			// TODO - Do something about CEOs
			takeoverComplete = true;
			message = "The attack was successful and the corp has been captured";

		} else {
			System.out.println("Defence success");
			target.setDefense(defendingRemaining);
			winner = BattleReportWinner.DEFENSE;
			message = "The attack was unsuccessful and the corp has not been captured";
		}

		BattleReport report = new BattleReport(now, attackVictory, defenseVictory, winner, takeoverComplete, source.getIp(), target.getIp(),
				defendingTroops, defendingRemaining, defendingKilled, defendingInTroops, defendingInRemaining, defendingInKilled, attackingTroops, attackingRemaining,
				attackingKilled, message);

		battleReportService.save(report);
		sourcePlayer.getLogs().getTakeoverLogs().add(report.get_id());
		targetPlayer.getLogs().getTakeoverLogs().add(report.get_id());
		//TODO - Add report to defence in players
		
		System.out.println(report);
		//TODO - Add a cooldown period for newly taken ips, or implement transfers

		locationService.save(source);
		locationService.save(target);
		playerService.save(sourcePlayer);
		playerService.save(targetPlayer);

		System.out.println("Attack complete");

	}

	private Troop getTroopsOfType(TroopType type, List<Troop> troops) {
		for (Troop troop : troops) {
			if (troop.getType().equals(type)) {
				return troop;
			}
		}
		return null;
	}

	private TransitTroop getTransitTroopOfType(TroopType type, List<TransitTroop> troops) {
		for (TransitTroop troop : troops) {
			if (troop.getType().equals(type)) {
				return troop;
			}
		}
		return null;
	}

	private List<TransitTroop> getTransitTroopsOfType(TroopType type, List<TransitTroop> troops) {
		List<TransitTroop> troopsOfType = new ArrayList<TransitTroop>();
		for (TransitTroop troop : troops) {
			if (troop.getType().equals(type)) {
				troopsOfType.add(troop);
			}
		}
		return troopsOfType;
	}

	private double randomAdjustment() {
		return 0.9 + (1.1 - 0.9) * new Random().nextDouble();
	}
}
