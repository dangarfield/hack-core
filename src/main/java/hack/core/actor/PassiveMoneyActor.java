package hack.core.actor;

import hack.core.actor.messages.PassiveMoneyMessage;
import hack.core.services.PassiveMoneyService;

import org.springframework.beans.factory.annotation.Autowired;

import akka.actor.UntypedActor;

public class PassiveMoneyActor extends UntypedActor {

	@Autowired
	private PassiveMoneyService passiveMoneyService;

	@Override
	public void onReceive(Object o) throws Exception {
		//TODO - Need to look at persistence and retrigger research events
		
		if (o instanceof PassiveMoneyMessage) {
			//PassiveMoneyMessage passivemMoneyMessage = (PassiveMoneyMessage) o;
			passiveMoneyService.incrementMoneyPassivelyFromActor();
		} else {
			System.out.println("UNKNOWN TYPE: " + o.getClass());
		}

	}
}
