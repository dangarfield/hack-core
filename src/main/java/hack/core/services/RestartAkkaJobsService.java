package hack.core.services;

import java.util.Date;
import java.util.List;

import hack.core.actor.config.ActorConfig;
import hack.core.actor.messages.AttackMessage;
import hack.core.actor.messages.DefenseMessage;
import hack.core.actor.messages.MissionMessage;
import hack.core.actor.messages.RecruitmentMessage;
import hack.core.actor.messages.ResearchMessage;
import hack.core.actor.messages.ReturnTroopsMessage;
import hack.core.dao.LocationDAO;
import hack.core.dao.MissionEntry;
import hack.core.dao.PlayerDAO;
import hack.core.dao.ResearchEntry;
import hack.core.dao.TakeoverTroopsEntry;
import hack.core.models.TrainingResearch;
import hack.core.models.TransitTroop;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RestartAkkaJobsService {

	@Autowired
	private LocationDAO locationDAO;
	@Autowired
	private PlayerDAO playerDAO;

	@Autowired
	private SchedulingService schedulingService;
	
	public void restartAllJobs() {
		restartRecruitment();
		restartTakeovers();
		restartDefense();
		restartReturning();
		restartResearch();
		restartMissions();
	}

	private void restartRecruitment() {
		
		System.out.println("restartRecruitment Started");
		List<RecruitmentMessage> messages = locationDAO.getRecruitingForRestart();
		
		for (RecruitmentMessage message : messages) {
			System.out.println("Schedule recruit - " + message.getIp() + " - " + message.getType() + " - " + message.getRecruitmentTime());
			schedulingService.scheduleJobOnce(ActorConfig.RECRUITMENT_ACTOR, message, message.getRecruitmentTime());
		}
		System.out.println("restartRecruitment Finished");
	}
	
	private void restartTakeovers() {
		System.out.println("restartTakeovers Started");
		List<TakeoverTroopsEntry> entries = locationDAO.getTransitTroopsAttackForRestart();
		
		for (TakeoverTroopsEntry entry : entries) {
			TransitTroop troop = entry.getTroops().get(0);
			
			ObjectId sourcePlayerId = playerDAO.getPlayerByLocationIP(troop.getSource()).getId();
			ObjectId targetPlayerId = playerDAO.getPlayerByLocationIP(troop.getTarget()).getId();
			AttackMessage attackMessage = new AttackMessage(sourcePlayerId, targetPlayerId, entry.getTroops(), 10000); //TODO - This won't work, not enough CEOs, for the time being, I'll set it 10000
			long secondsToAttack = (troop.getArrival().getTime() - new Date().getTime()) / 1000;
			if(secondsToAttack < 0) {
				secondsToAttack = 0;
			}
			System.out.println("Schedule takeover - " + troop.getSource() + " to " + troop.getTarget() + " in " + secondsToAttack + " seconds");
			schedulingService.scheduleJobOnce(ActorConfig.ATTACK_ACTOR, attackMessage, secondsToAttack);
		}
		System.out.println("restartTakeovers Finished");
	}
	private void restartDefense() {
		System.out.println("restartDefense Started");
		List<TakeoverTroopsEntry> entries = locationDAO.getTransitTroopsDefenseForRestart();
		
		for (TakeoverTroopsEntry entry : entries) {
			TransitTroop troop = entry.getTroops().get(0);
			DefenseMessage defenseMessage = new DefenseMessage(entry.getTroops());
			long secondsToAttack = (troop.getArrival().getTime() - new Date().getTime()) / 1000;
			if(secondsToAttack < 0) {
				secondsToAttack = 0;
			}
			System.out.println("Schedule defense - " + troop.getSource() + " to " + troop.getTarget() + " in " + secondsToAttack + " seconds");
			schedulingService.scheduleJobOnce(ActorConfig.DEFENSE_ACTOR, defenseMessage, secondsToAttack);
		}
		System.out.println("restartDefense Finished");
	}
	private void restartReturning() {
		System.out.println("restartReturning Started");
		List<TakeoverTroopsEntry> entries = locationDAO.getTransitTroopsReturningForRestart();
		
		for (TakeoverTroopsEntry entry : entries) {
			TransitTroop troop = entry.getTroops().get(0);
			ReturnTroopsMessage returnTroopMessage = new ReturnTroopsMessage(entry.getTroops());
			long secondsToAttack = (troop.getArrival().getTime() - new Date().getTime()) / 1000;
			if(secondsToAttack < 0) {
				secondsToAttack = 0;
			}
			System.out.println("Schedule returning - " + troop.getSource() + " to " + troop.getTarget() + " in " + secondsToAttack + " seconds");
			schedulingService.scheduleJobOnce(ActorConfig.RETURN_TROOPS_ACTOR, returnTroopMessage, secondsToAttack);
		}
		System.out.println("restartReturning Finished");
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
			schedulingService.scheduleJobOnce(ActorConfig.RESEARCH_TRAINING_ACTOR, researchMessage, secondsToCompletion);
		}
		System.out.println("restartResearch Finished");
	}
	
	private void restartMissions() {
		System.out.println("restartMissions Started");
		List<MissionEntry> missions = playerDAO.getMissionsForRestart();
		
		for (MissionEntry mission : missions) {
			long secondsToCompletion = (mission.getCompletionTime().getTime() - new Date().getTime()) / 1000;
			if(secondsToCompletion < 0) {
				secondsToCompletion = 0;
			}
			MissionMessage missionMessage = new MissionMessage(mission.getPlayerId(), mission.getType());
			System.out.println("Schedule mission - " + missionMessage.getPlayerId() + " - " + missionMessage.getType() + " in " + secondsToCompletion + " seconds");
			schedulingService.scheduleJobOnce(ActorConfig.MISSION_ACTOR, missionMessage, secondsToCompletion);
		}
		
		
		System.out.println("restartMissions Finished");
	}
	
}
