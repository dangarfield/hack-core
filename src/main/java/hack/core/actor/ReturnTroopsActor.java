package hack.core.actor;

import hack.core.actor.messages.ReturnTroopsMessage;
import hack.core.services.AttackService;

import org.springframework.beans.factory.annotation.Autowired;

import akka.actor.UntypedActor;

public class ReturnTroopsActor extends UntypedActor {

	@Autowired
	private AttackService attackService;

	@Override
	public void onReceive(Object o) throws Exception {
		if (o instanceof ReturnTroopsMessage) {
			ReturnTroopsMessage returnTroopMessage = (ReturnTroopsMessage) o;
			attackService.completeReturnTroopTransit(returnTroopMessage);
		} else {
			System.out.println("UNKNOWN TYPE: " + o.getClass());
		}

	}
}
