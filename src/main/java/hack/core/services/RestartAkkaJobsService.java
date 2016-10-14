package hack.core.services;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import hack.core.actor.config.ActorConfig;
import hack.core.actor.messages.AttackMessage;
import hack.core.actor.messages.RecruitmentMessage;
import hack.core.actor.messages.ResearchMessage;
import hack.core.dao.LocationDAO;
import hack.core.dao.PlayerDAO;
import hack.core.dao.ResearchEntry;
import hack.core.dao.TakeoverTroopsEntry;
import hack.core.models.TrainingResearch;
import hack.core.models.TransitTroop;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import scala.concurrent.util.Duration;

@Service
public class RestartAkkaJobsService {

	@Autowired
	private LocationDAO locationDAO;
	@Autowired
	private PlayerDAO playerDAO;

	@Autowired
	@Qualifier(ActorConfig.RESEARCH_TRAINING_ACTOR)
	private ActorRef researchTrainingActor;
	@Autowired
	@Qualifier(ActorConfig.RECRUITMENT_ACTOR)
	private ActorRef recruitmentActor;
	@Autowired
	@Qualifier(ActorConfig.ATTACK_ACTOR)
	private ActorRef attackActor;
	@Autowired
	private ActorSystem actorSystem;
	
	public void restartAllJobs() {
		restartRecruitment();
		restartTakeovers();
		restartResearch();
	}

	private void restartRecruitment() {
		
		System.out.println("restartRecruitment Started");
		List<RecruitmentMessage> messages = locationDAO.getRecruitingForRestart();
		
		for (RecruitmentMessage message : messages) {
			System.out.println("Schedule recruit - " + message.getIp() + " - " + message.getType() + " - " + message.getRecruitmentTime());
			actorSystem.scheduler().scheduleOnce(Duration.create(message.getRecruitmentTime(), TimeUnit.SECONDS), recruitmentActor, message);	
		}
		System.out.println("restartRecruitment Finished");
	}
	
	private void restartTakeovers() {
		System.out.println("restartTakeovers Started");
		List<TakeoverTroopsEntry> entries = locationDAO.getTransitTroopsForRestart();
		
		for (TakeoverTroopsEntry entry : entries) {
			TransitTroop troop = entry.getTroops().get(0);
			
			ObjectId sourcePlayerId = playerDAO.getPlayerByLocationIP(troop.getSource()).getId();
			ObjectId targetPlayerId = playerDAO.getPlayerByLocationIP(troop.getTarget()).getId();
			AttackMessage attackMessage = new AttackMessage(sourcePlayerId, targetPlayerId, entry.getTroops(), entry.getCeo());
			long secondsToAttack = (troop.getArrival().getTime() - new Date().getTime()) / 1000;
			if(secondsToAttack < 0) {
				secondsToAttack = 0;
			}
			System.out.println("Schedule takeover - " + troop.getSource() + " to " + troop.getTarget() + " in " + secondsToAttack + " seconds");
			actorSystem.scheduler().scheduleOnce(Duration.create(secondsToAttack, TimeUnit.SECONDS), attackActor, attackMessage);
		}
		System.out.println("restartTakeovers Finished");
	}
	
	private void restartResearch() {
		System.out.println("restartResearch Started");
		List<ResearchEntry> researches = playerDAO.getResearchesForRestart();
		
		for (ResearchEntry researchEntry : researches) {
			TrainingResearch trainingResearch = researchEntry.getTrainingResearch();
			ResearchMessage researchMessage = researchEntry.getResearchMessage();
			long secondsToCompletion = (trainingResearch.getEndTime().getTime() - new Date().getTime()) / 1000;
			if(secondsToCompletion < 0) {
				secondsToCompletion = 0;
			}
			System.out.println("Schedule research - " + researchMessage.getPlayerEmail() + " - " + researchMessage.getType() + " in " + secondsToCompletion + " seconds");
			actorSystem.scheduler().scheduleOnce(Duration.create(secondsToCompletion, TimeUnit.SECONDS), researchTrainingActor, researchMessage);
		}
	}
	
	
}
