package hack.core.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hack.core.actor.config.ActorConfig;
import hack.core.actor.messages.RecruitmentMessage;
import hack.core.dao.LocationDAO;
import hack.core.dao.PlayerDAO;
import hack.core.dto.APIResultDTO;
import hack.core.dto.APIResultType;
import hack.core.models.Location;
import hack.core.models.Player;
import hack.core.models.ResearchType;
import hack.core.models.TrainingTroop;
import hack.core.models.Troop;
import hack.core.models.TroopType;

@Service
public class RecruitmentService {

	//private static final Logger LOG = Logger.getLogger("RecruitmentService");
	
	@Autowired
	private LocationDAO locationDAO;
	@Autowired
	private PlayerDAO playerDAO;
	@Autowired
	private LocationService locationService;
	@Autowired
	private SchedulingService schedulingService;

	public APIResultDTO recruit(Player player, String ip, TroopType type, long desiredNoOfTroops) {

		//Validate ip
		Location location = locationService.getLocationFromIp(ip);
		if(location == null || !location.getPlayer().equals(player.getId())) {
			return new APIResultDTO(APIResultType.ERROR, "You do not own the ip: " + ip);
		}
		
		//Validate troop type
		//Already done, maybe bring it here
		
		//Validate max no of troops
		long maxTroops = getMaxTroopNumber(player);
		long currentTroops = location.getRecruiting().stream().mapToLong(Troop::getNoOfTroops).sum();
		long maxAllowed = maxTroops - currentTroops;
		if(maxAllowed < desiredNoOfTroops) {
			return new APIResultDTO(APIResultType.ERROR, "You can currently recruit a maximum of " + maxAllowed + " at this ip");
		}
		
		//Calculate recruitment cost
		long costPerTroop = recruitmentCostPerTroop(player.researchOfType(ResearchType.RECRUITMENT_COST).getLevel());
		long totalCost = costPerTroop * desiredNoOfTroops;
		if(player.getMoney() < totalCost) {
			return new APIResultDTO(APIResultType.ERROR, "Not enough money. You need £"+totalCost + " - You currently have £"+player.getMoney());
		}
		player.setMoney(player.getMoney() - totalCost);
		
		//Calculate recruitment speed
		long timePerTroop = recruitmentTimePerTroop(player.researchOfType(ResearchType.RECRUITMENT_SPEED).getLevel());
		timePerTroop = 10;
		String message = "Recruiting " + desiredNoOfTroops + " will take you " + timePerTroop + " seconds and cost £" + totalCost;
		
		boolean troopRecruitmentFound = false;
		for (TrainingTroop troop : location.getRecruiting()) {
			if(troop.getType().equals(type)) {
				troop.setNoOfTroops(troop.getNoOfTroops() + desiredNoOfTroops);
				troop.setRecruitmentTime(timePerTroop);
				troopRecruitmentFound = true;
				break;
			}
		}
		if(!troopRecruitmentFound) {
			location.getRecruiting().add(new TrainingTroop(type, desiredNoOfTroops,timePerTroop));
		}
		locationDAO.saveLocation(location);
		playerDAO.save(player);
		
		RecruitmentMessage recruitmentMessage = new RecruitmentMessage(ip, type, timePerTroop);
		
		//Set scheduler
		schedulingService.scheduleJobOnce(ActorConfig.RECRUITMENT_ACTOR, recruitmentMessage, timePerTroop);
		return new APIResultDTO(APIResultType.SUCCESS, message);
	}
	
	private long recruitmentTimePerTroop(int level) {
		
		long maxTime = 60;
		long minTime = 30;
		
		long diffPerLevel = (maxTime - minTime) / 10;
		
		long seconds = maxTime - (diffPerLevel * level);
		
		// (level * 8) + 60 * weight (1.2 ish) seconds
		
		return seconds;
	}
	
	private long recruitmentCostPerTroop(int level) {

		long maxCost = 10000;
		long minCost = 5000;
		
		
		long diffPerLevel = (maxCost - minCost) / 10;
		
		long cost = maxCost - (diffPerLevel * level);

		// TODO - add weighting based on TroopType
		return cost;
	}

	private int getMaxTroopNumber(Player player) {
		return player.researchOfType(ResearchType.RECRUITMENT_PARALLEL).getLevel() * 100;
	}

	public void completeRecruitment(RecruitmentMessage recruitmentMessage) {
		System.out.println("Single unit recruitment: start");
		Location location = locationDAO.getLocationFromIp(recruitmentMessage.getIp());

		for (Troop troop : location.getDefense()) {
			if(troop.getType().equals(recruitmentMessage.getType())) {
				troop.setNoOfTroops(troop.getNoOfTroops() + 1);
				break;
			}
		}
		
		for (Troop troop : location.getRecruiting()) {
			if(troop.getType().equals(recruitmentMessage.getType())) {
				troop.setNoOfTroops(troop.getNoOfTroops() - 1);
				if(troop.getNoOfTroops() > 0) {
					schedulingService.scheduleJobOnce(ActorConfig.RECRUITMENT_ACTOR, recruitmentMessage, recruitmentMessage.getRecruitmentTime());
					System.out.println("Recruit more");
				} else {
					System.out.println("Recruiting finished");
					location.getRecruiting().remove(troop);
				}
				break;
			}
		}
		locationDAO.saveLocation(location);
	}
}
