package hack.core.actor.config;

import hack.core.actor.ResearchTrainingActor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

@Configuration
public class ActorConfig {

	public static final String RESEARCH_TRAINING_ACTOR = "research-training-actor";
	public static final String ACTOR_SYSTEM = "hack-actor-actorSystem";

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
}
