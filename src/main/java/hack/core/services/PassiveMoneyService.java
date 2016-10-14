package hack.core.services;

import java.util.concurrent.TimeUnit;

import hack.core.actor.config.ActorConfig;
import hack.core.actor.messages.PassiveMoneyMessage;
import hack.core.dao.PlayerDAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import scala.concurrent.util.Duration;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;

@Service
public class PassiveMoneyService {

	@Autowired
	private PlayerDAO playerDAO;
	@Autowired
	@Qualifier(ActorConfig.PASSIVE_MONEY_ACTOR)
	private ActorRef passiveMoneyActor;
	@Autowired
	private ActorSystem actorSystem;

	private static int PASSIVE_MONEY_DURATION_IN_SECONDS = 6;
	
	public void initialisePassiveMoney() {
		System.out.println("Initialising passive money every " + PASSIVE_MONEY_DURATION_IN_SECONDS + " seconds");
		actorSystem.scheduler().schedule(Duration.create(0, TimeUnit.SECONDS), Duration.create(PASSIVE_MONEY_DURATION_IN_SECONDS, TimeUnit.SECONDS), passiveMoneyActor, new PassiveMoneyMessage());
	}
	
	public void incrementMoneyPassivelyFromActor() {
		for (int level = 1; level <= 10; level++) {
			long amount = getPassiveMoneyAmountForLevel(level, PASSIVE_MONEY_DURATION_IN_SECONDS);
			playerDAO.incrementMoneyByPassiveMoneyLevel(level, amount);
		}
	}

	private long getPassiveMoneyAmountForLevel(int level, int seconds) {
		float factor = ((float)seconds) / 3600F;
		System.out.println(factor);
		return (long) ((level * level * 5000) * factor);
	}
	public static void main(String[] args) {
		int[] times = {1,6,10,60,60*60};
		
		for (int level = 1; level <= 10; level++) {
			for (int time : times) {
				long amount = new PassiveMoneyService().getPassiveMoneyAmountForLevel(level, time);
				System.out.println("Passive Money Level "+ level + " - " + time + " seconds - £" + amount);
			}
			
		}
	}
}
