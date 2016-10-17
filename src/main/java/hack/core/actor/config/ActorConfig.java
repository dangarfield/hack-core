package hack.core.actor.config;

import hack.core.actor.AttackActor;
import hack.core.actor.DefenseActor;
import hack.core.actor.MissionActor;
import hack.core.actor.PassiveMoneyActor;
import hack.core.actor.ResearchTrainingActor;
import hack.core.actor.RecruitmentActor;
import hack.core.actor.ReturnTroopsActor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

@Configuration
public class ActorConfig {

	public static final String ACTOR_SYSTEM = "hack-actor-actorSystem";
	public static final String RESEARCH_TRAINING_ACTOR = "research-training-actor";
	public static final String ATTACK_ACTOR = "attack-actor";
	public static final String DEFENSE_ACTOR = "defense-actor";
	public static final String RETURN_TROOPS_ACTOR = "return-troops-actor";
	public static final String RECRUITMENT_ACTOR = "recuirtment-actor";
	public static final String PASSIVE_MONEY_ACTOR = "passive-money-actor";
	public static final String MISSION_ACTOR = "mission-actor";

	private ActorSystem actorSystem;

	@Autowired
	private ApplicationContext applicationContext;

	@Bean(name = ACTOR_SYSTEM, destroyMethod = "shutdown")
	public ActorSystem actorSystem() {
		actorSystem = ActorSystem.create(ACTOR_SYSTEM);
		return actorSystem;
	}

	@Bean(name = RESEARCH_TRAINING_ACTOR)
	@DependsOn({ ACTOR_SYSTEM })
	public ActorRef businessActor() {
		return actorSystem.actorOf(//
				new DependencyInjectionProps(applicationContext, ResearchTrainingActor.class), RESEARCH_TRAINING_ACTOR);
	}
	@Bean(name = ATTACK_ACTOR)
	@DependsOn({ ACTOR_SYSTEM })
	public ActorRef attackActor() {
		return actorSystem.actorOf(//
				new DependencyInjectionProps(applicationContext, AttackActor.class), ATTACK_ACTOR);
	}
	@Bean(name = DEFENSE_ACTOR)
	@DependsOn({ ACTOR_SYSTEM })
	public ActorRef defenseActor() {
		return actorSystem.actorOf(//
				new DependencyInjectionProps(applicationContext, DefenseActor.class), DEFENSE_ACTOR);
	}
	@Bean(name = RETURN_TROOPS_ACTOR)
	@DependsOn({ ACTOR_SYSTEM })
	public ActorRef returnTroopsActor() {
		return actorSystem.actorOf(//
				new DependencyInjectionProps(applicationContext, ReturnTroopsActor.class), RETURN_TROOPS_ACTOR);
	}
	@Bean(name = RECRUITMENT_ACTOR)
	@DependsOn({ ACTOR_SYSTEM })
	public ActorRef recruitmentActor() {
		return actorSystem.actorOf(//
				new DependencyInjectionProps(applicationContext, RecruitmentActor.class), RECRUITMENT_ACTOR);
	}
	@Bean(name = PASSIVE_MONEY_ACTOR)
	@DependsOn({ ACTOR_SYSTEM })
	public ActorRef passiveMoneyActor() {
		return actorSystem.actorOf(//
				new DependencyInjectionProps(applicationContext, PassiveMoneyActor.class), PASSIVE_MONEY_ACTOR);
	}
	@Bean(name = MISSION_ACTOR)
	@DependsOn({ ACTOR_SYSTEM })
	public ActorRef missionActor() {
		return actorSystem.actorOf(//
				new DependencyInjectionProps(applicationContext, MissionActor.class), MISSION_ACTOR);
	}
}
