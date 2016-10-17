package hack.core.services;


import hack.core.actor.config.ActorConfig;
import hack.core.actor.messages.PassiveMoneyMessage;
import hack.core.dao.PlayerDAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PassiveMoneyService {

	@Autowired
	private PlayerDAO playerDAO;
	@Autowired
	private SchedulingService schedulingService;

	private static int PASSIVE_MONEY_DURATION_IN_SECONDS = 6;
	
	public void initialisePassiveMoney() {
		System.out.println("Initialising passive money every " + PASSIVE_MONEY_DURATION_IN_SECONDS + " seconds");
		schedulingService.scheduleJobRepeating(ActorConfig.PASSIVE_MONEY_ACTOR, new PassiveMoneyMessage(), PASSIVE_MONEY_DURATION_IN_SECONDS);
	}
	
	public void incrementMoneyPassivelyFromActor() {
		System.out.println("--- Passive Money Service ---");
		for (int level = 1; level <= 10; level++) {
			long amount = getPassiveMoneyAmountForLevel(level, PASSIVE_MONEY_DURATION_IN_SECONDS);
			playerDAO.incrementMoneyByPassiveMoneyLevel(level, amount);
		}
	}

	private long getPassiveMoneyAmountForLevel(int level, int seconds) {
		float factor = ((float)seconds) / 3600F;
//		System.out.println(factor);
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
