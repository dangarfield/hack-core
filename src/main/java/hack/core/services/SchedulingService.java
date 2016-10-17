package hack.core.services;

import java.util.concurrent.TimeUnit;

import hack.core.actor.config.ActorConfig;
import hack.core.actor.messages.AbstractMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import scala.concurrent.util.Duration;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;

@Service
public class SchedulingService {

	@Autowired
	@Qualifier(ActorConfig.MISSION_ACTOR)
	private ActorRef missionActor;
	@Autowired
	@Qualifier(ActorConfig.PASSIVE_MONEY_ACTOR)
	private ActorRef passiveMoneyActor;
	@Autowired
	@Qualifier(ActorConfig.RESEARCH_TRAINING_ACTOR)
	private ActorRef researchTrainingActor;
	@Autowired
	@Qualifier(ActorConfig.RECRUITMENT_ACTOR)
	private ActorRef recruitmentActor;
	@Autowired
	@Qualifier(ActorConfig.RETURN_TROOPS_ACTOR)
	private ActorRef returnTroopsActor;
	@Autowired
	@Qualifier(ActorConfig.DEFENSE_ACTOR)
	private ActorRef defenseActor;
	@Autowired
	@Qualifier(ActorConfig.ATTACK_ACTOR)
	private ActorRef attackActor;
	@Autowired
	private ActorSystem actorSystem;
	
	public void scheduleJobOnce(String actorName, AbstractMessage message, long delayTimeInSeconds) {
		ActorRef actor = getActorFromName(actorName);
		actorSystem.scheduler().scheduleOnce(Duration.create(delayTimeInSeconds, TimeUnit.SECONDS), actor, message);
	}
	public void scheduleJobRepeating(String actorName, AbstractMessage message, long delayTimeInSeconds) {
		ActorRef actor = getActorFromName(actorName);
		actorSystem.scheduler().schedule(Duration.create(0, TimeUnit.SECONDS), Duration.create(delayTimeInSeconds, TimeUnit.SECONDS), actor, message);
	}
	
	private ActorRef getActorFromName(String actorName) {
		ActorRef actor = null;
		switch (actorName) {
		case ActorConfig.ATTACK_ACTOR:
			actor = attackActor;
			break;
		case ActorConfig.DEFENSE_ACTOR:
			actor = defenseActor;
			break;
		case ActorConfig.RETURN_TROOPS_ACTOR:
			actor = returnTroopsActor;
			break;
		case ActorConfig.MISSION_ACTOR:
			actor = missionActor;
			break;
		case ActorConfig.RESEARCH_TRAINING_ACTOR:
			actor = researchTrainingActor;
			break;
		case ActorConfig.RECRUITMENT_ACTOR:
			actor = recruitmentActor;
			break;
		case ActorConfig.PASSIVE_MONEY_ACTOR:
			actor = passiveMoneyActor;
			break;

		default:
			break;
		}
		return actor;
	}
}
