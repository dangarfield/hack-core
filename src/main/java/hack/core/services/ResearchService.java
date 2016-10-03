package hack.core.services;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import scala.concurrent.util.Duration;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import hack.core.actor.config.ActorConfig;
import hack.core.actor.messages.ResearchMessage;
import hack.core.dao.PlayerDAO;
import hack.core.dto.APIResultDTO;
import hack.core.dto.APIResultType;
import hack.core.models.Player;
import hack.core.models.Research;
import hack.core.models.ResearchType;
import hack.core.models.TrainingResearch;

@Service
public class ResearchService {

	private static final Logger LOG = Logger.getLogger("ResearchService");
	
	@Autowired
	private PlayerDAO playerDAO;
	@Autowired
	private LocationService locationService;

	@Autowired
	@Qualifier(ActorConfig.RESEARCH_TRAINING_ACTOR)
	private ActorRef researchTrainingActor;

	@Autowired
	private ActorSystem actorSystem;

	public APIResultDTO triggerUpgrade(Player player, ResearchType researchType) {

		// Get research
		Research research = player.researchOfType(researchType);
		if (research == null) {
			return new APIResultDTO(APIResultType.ERROR, "Unknown research type: " + researchType);
		}
		// Check current research level
		int maxLevel = maxResearchLevel(player);
		int desiredLevelMinusOne = research.getLevel() + research.getCurrentlyTraining().size();
		if (maxLevel <= desiredLevelMinusOne) {
			return new APIResultDTO(APIResultType.ERROR, "Your current max research level is " + maxLevel);
		}

		// Check money
		long cost = researchUpgradeCost(research.getType(), desiredLevelMinusOne);
		if (cost > player.getMoney()) {
			return new APIResultDTO(APIResultType.ERROR, "Not enough money, you need £" + cost);
		}

		// Check max concurrent upgrades
		int currentUpgradesInProgress = player.currentNoOfResearchInTraining();
		int maxParallelResearchTraining = maxParallelResearchTraining(player);
		if (currentUpgradesInProgress >= maxParallelResearchTraining) {
			return new APIResultDTO(APIResultType.ERROR, "Not enough upgrade slots, current max is " + maxParallelResearchTraining);
		}
		// Reduce money
		player.setMoney(player.getMoney() - cost);

		// Calculate time for training
		int trainingTime = researchUpgradeTime(research.getType(), desiredLevelMinusOne);
		Date startTime = new Date();
		Date endTime = new Date(startTime.getTime() + (trainingTime * 1000));

		// Create TrainingResearch and add to player
		TrainingResearch trainingResearch = new TrainingResearch(new ObjectId().toString(), startTime, endTime);
		research.getCurrentlyTraining().add(trainingResearch);

		// Save player
		playerDAO.save(player);

		// Trigger research actor
		ResearchMessage researchMessage = new ResearchMessage(player.getEmail(), researchType, trainingResearch.getId());
		actorSystem.scheduler().scheduleOnce(Duration.create(trainingTime, TimeUnit.SECONDS), researchTrainingActor, researchMessage);

		return new APIResultDTO(APIResultType.SUCCESS, "Research training to level " + (desiredLevelMinusOne + 1));
	}

	public void completeTraining(ResearchMessage researchMessage) {
		Player player = playerDAO.getUserByEmail(researchMessage.getPlayerEmail());
		Research research = player.researchOfType(researchMessage.getType());
		research.setLevel(research.getLevel() + 1);
		TrainingResearch currentTraining = research.getCurrentTrainingByID(researchMessage.getId());
		research.getCurrentlyTraining().remove(currentTraining);
		playerDAO.save(player);
		if(research.getType().equals(ResearchType.RESEARCH_HIDE)) {
			locationService.incrementAllOfAPlayersLocationsResearchHideLevel(player);
		}
		LOG.info("Completed training: " + player.getEmail() + " - " + research.getType() + " - " + research.getLevel());
	}

	private long researchUpgradeCost(ResearchType type, int level) {
		// level squared * weight (1.2 ish) * research

		long cost = 5000 + (level * level);

		// TODO - add research type weight
		// TODO - add learned research weight
		return cost;
	}

	private int researchUpgradeTime(ResearchType type, int level) {
		// (level * 8) + 60 * weight (1.2 ish) seconds
		int seconds = 60 + (level * 8);

		// TODO - add research type weight
		// TODO - add learned research weight
		return seconds;
	}

	private int maxResearchLevel(Player player) {
		// TODO - Not all researches want to be exponentially increased
		
		return player.getLocationIps().size() * 20;
	}

	private int maxParallelResearchTraining(Player player) {
		return player.researchOfType(ResearchType.UPGRADE_PARALLEL).getLevel();
	}
}
