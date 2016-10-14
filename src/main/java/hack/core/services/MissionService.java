package hack.core.services;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import hack.core.actor.config.ActorConfig;
import hack.core.actor.messages.MissionMessage;
import hack.core.dto.APIResultDTO;
import hack.core.dto.APIResultType;
import hack.core.models.Location;
import hack.core.models.MissionInProgress;
import hack.core.models.MissionType;
import hack.core.models.Player;
import hack.core.models.Research;
import hack.core.models.ResearchType;
import hack.core.models.Troop;
import hack.core.models.TroopType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import scala.concurrent.util.Duration;

@Service
public class MissionService {

	@Autowired
	private PlayerService playerService;
	@Autowired
	private LocationService locationService;
	@Autowired
	private ResearchService researchService;
	
	@Autowired
	@Qualifier(ActorConfig.MISSION_ACTOR)
	private ActorRef missionActor;
	@Autowired
	private ActorSystem actorSystem;
	
	public APIResultDTO triggerMission(Player player, MissionType missionType) {

		int playersMissionLevel = player.researchOfType(ResearchType.MISSIONS_LEVEL).getLevel();

		int factor = getFactorForMission(missionType);

		// Validate level for mission
		if (playersMissionLevel < factor) {
			return new APIResultDTO(APIResultType.ERROR, "You have not levelled up your mission research ");
		}

		// Check prerequisites (eg, money, troops etc)
		// TODO - This is just money at the minute
		long requiredMoney = factor * 100000;
		if (player.getMoney() < requiredMoney) {
			return new APIResultDTO(APIResultType.ERROR, "Not enough money, you need £" + requiredMoney);
		}

		// Check if mission is already running
		if (isMissionAlreadyRunning(player, missionType)) {
			return new APIResultDTO(APIResultType.ERROR, "You are already running this mission");
		}

		// Reduce prerequisites
		player.setMoney(player.getMoney() - requiredMoney);

		// Calculate time for completion
		long timeInSeconds = factor * 60 * 60; // 1 hour per mission level
		timeInSeconds = 10;
		Date startTime = new Date();
		Date endTime = new Date(startTime.getTime() + (timeInSeconds * 1000));

		// Create MissionInProgress and add to player
		player.getMissions().add(new MissionInProgress(missionType, startTime, endTime));

		// Save player
		playerService.save(player);

		// Trigger Mission Actor
		MissionMessage missionMessage = new MissionMessage(player.getId(), missionType);
		actorSystem.scheduler().scheduleOnce(Duration.create(timeInSeconds, TimeUnit.SECONDS), missionActor, missionMessage);

		return new APIResultDTO(APIResultType.SUCCESS, missionType + " Mission Started. Completion time: " + endTime);
	}

	private boolean isMissionAlreadyRunning(Player player, MissionType missionType) {
		return player.getMissions().stream().anyMatch(m -> m.getType().equals(missionType));
	}

	private int getFactorForMission(MissionType missionType) {
		switch (missionType) {
		case CEO:
			return 1;
		case MONEY_SMALL:
			return 2;
		case TROOPS_SMALL:
			return 3;
		case UPGRADES_SMALL:
			return 4;
		case MONEY_MEDIUM:
			return 5;
		case TROOPS_MEDIUM:
			return 6;
		case UPGRADES_MEDIUM:
			return 7;
		case MONEY_LARGE:
			return 8;
		case TROOPS_LARGE:
			return 9;
		case UPGRADES_LARGE:
			return 10;
		default:
			break;
		}
		return 0;
	}

	public void completeMission(MissionMessage missionMessage) {
		Player player = playerService.getPlayerById(missionMessage.getPlayerId());
		System.out.println("Completing mission: " + missionMessage.getType() + " for player " + player.getEmail());
		
		long extraMoney;
		Location location;
		int troopCount, upgradeLevel;
		TroopType troopType;
		ResearchType researchType;
		Research research;
		
		// TODO - Add a mission log
		switch (missionMessage.getType()) {
		case CEO:
			player.setCeoCount(player.getCeoCount() + 1);
			break;
		case MONEY_SMALL:
			extraMoney = randomLong(100000,500000);
			player.setMoney(player.getMoney() + extraMoney);
			break;
		case TROOPS_SMALL:
			location = locationService.getRandomLocationForPlayer(player);
			troopCount = randomInt(1, 10);
			troopType = randomTroopType();
			addTroopsInLocation(location, troopCount, troopType);
			locationService.save(location);
			break;
		case UPGRADES_SMALL:
			upgradeLevel = randomInt(1, 4);
			researchType = randomResearchType();
			research = player.researchOfType(researchType);
			research.setLevel(research.getLevel() + upgradeLevel);
			researchService.amendWhereResearchUpgradeAffectsNonPlayerLevel(researchType, player, upgradeLevel);
			break;
		case MONEY_MEDIUM:
			extraMoney = randomLong(300000,1000000);
			player.setMoney(player.getMoney() + extraMoney);
			break;
		case TROOPS_MEDIUM:
			location = locationService.getRandomLocationForPlayer(player);
			troopCount = randomInt(10, 100);
			troopType = randomTroopType();
			addTroopsInLocation(location, troopCount, troopType);
			locationService.save(location);
			break;
		case UPGRADES_MEDIUM:
			upgradeLevel = randomInt(5, 8);
			researchType = randomResearchType();
			research = player.researchOfType(researchType);
			research.setLevel(research.getLevel() + upgradeLevel);
			researchService.amendWhereResearchUpgradeAffectsNonPlayerLevel(researchType, player, upgradeLevel);
			break;
		case MONEY_LARGE:
			extraMoney = randomLong(800000,2000000);
			player.setMoney(player.getMoney() + extraMoney);
			break;
		case TROOPS_LARGE:
			location = locationService.getRandomLocationForPlayer(player);
			troopCount = randomInt(100, 1000);
			troopType = randomTroopType();
			addTroopsInLocation(location, troopCount, troopType);
			locationService.save(location);
			break;
		case UPGRADES_LARGE:
			upgradeLevel = randomInt(9, 12);
			researchType = randomResearchType();
			researchType = randomResearchType();
			research = player.researchOfType(researchType);
			research.setLevel(research.getLevel() + upgradeLevel);
			researchService.amendWhereResearchUpgradeAffectsNonPlayerLevel(researchType, player, upgradeLevel);
			break;
		default:
			break;
		}
		
		player.setMissions(player.getMissions().stream().filter(m -> !m.getType().equals(missionMessage.getType())).collect(Collectors.toList()));
		
		playerService.save(player);
	}

	private void addTroopsInLocation(Location location, int troopCount, TroopType troopType) {
		boolean found = false;
		for (Troop troop : location.getDefense()) {
			if(troop.getType().equals(troopType)) {
				found = true;
				troop.setNoOfTroops(troop.getNoOfTroops() + troopCount);
				break;
			}
		}
		if(!found) {
			location.getDefense().add(new Troop(troopType, troopCount));
		}
	}

	private long randomLong(long rangeMin, long rangeMax) {
		return ThreadLocalRandom.current().nextLong(rangeMin, rangeMax);
	}
	private int randomInt(int rangeMin, int rangeMax) {
		return ThreadLocalRandom.current().nextInt(rangeMin, rangeMax);
	}

	private TroopType randomTroopType() {
		TroopType[] values = TroopType.values();
		int size = values.length;
		return values[new Random().nextInt(size)];
	}
	private ResearchType randomResearchType() {
		ResearchType[] values = ResearchType.values();
		int size = values.length;
		ResearchType researchType = values[new Random().nextInt(size)];
		if(researchService.isResearchTypeMaxLevelCapped(researchType)) {
			return randomResearchType();
		}
		return researchType;
	}
}
