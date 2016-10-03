package hack.core.services;

import java.util.Date;

import hack.core.dto.APIResultDTO;
import hack.core.dto.APIResultType;
import hack.core.models.AttackLog;
import hack.core.models.AttackType;
import hack.core.models.Player;
import hack.core.models.ResearchType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AttackService {

	@Autowired
	private PlayerService playerService;

	public APIResultDTO stealMoneyAttack(Player source, String targetIp) {
		
		//Validate IP
		Player targetPlayer = playerService.getPlayerByLocationIP(targetIp);
		if (targetPlayer == null) {
			return new APIResultDTO(APIResultType.ERROR, "Unknown IP: " + targetIp);
		}
		
		APIResultType result = APIResultType.WARNING;
		String message = "Error, something went wrong";
		
		// TODO - Have I attacked recently?
		if(targetPlayer.getRecentAttackForIp(targetIp) != null) {
			return new APIResultDTO(APIResultType.WARNING, "You have recently attacked: " + targetIp);
		}
		
		// Who won?
		int attack = source.researchOfType(ResearchType.MONEY_ATTACK).getLevel();
		int defense = targetPlayer.researchOfType(ResearchType.MONEY_DEFENSE).getLevel();

		if (attack > defense) {
			int amount = (int) Math.ceil(targetPlayer.getMoney() / 100.0 * 35.0); //35%
			source.setMoney(source.getMoney() + amount);
			targetPlayer.setMoney(targetPlayer.getMoney() - amount);
			result = APIResultType.SUCCESS;
			message = "Gained £"+amount;
		} else {
			message = "Attack failed";
		}
		AttackLog attackLog = new AttackLog(AttackType.STEAL_MONEY, new Date(), source.getName(), targetIp, result, message);
		
		source.getStealMoneyAttackCooldown().add(attackLog);
		playerService.save(source);
		targetPlayer.getStealMoneyAttackCooldown().add(attackLog);
		playerService.save(targetPlayer);
		
		// TODO - ensure that you have to wait to attack someone again
		
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
		int attack = source.researchOfType(ResearchType.RESEARCH_HIDE).getLevel();
		int defense = targetPlayer.researchOfType(ResearchType.RESEARCH_HIDE).getLevel();

		if (attack > defense) {
			result = APIResultType.SUCCESS;
			message = "Scan successful - " + targetPlayer.getName() + " - Defense level: "+targetPlayer.researchOfType(ResearchType.MONEY_DEFENSE).getLevel(); //TODO - Add proper details 
		} else {
			message = "Scan failed";
		}
		return new APIResultDTO(result, message);
	}
}
