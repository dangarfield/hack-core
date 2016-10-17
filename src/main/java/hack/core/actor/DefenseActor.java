package hack.core.actor;

import hack.core.actor.messages.DefenseMessage;
import hack.core.services.AttackService;

import org.springframework.beans.factory.annotation.Autowired;

import akka.actor.UntypedActor;

public class DefenseActor extends UntypedActor {

	@Autowired
	private AttackService attackService;

	@Override
	public void onReceive(Object o) throws Exception {
		//TODO - Need to look at persistence and retrigger research events
		
		if (o instanceof DefenseMessage) {
			DefenseMessage defenseMessage = (DefenseMessage) o;
			attackService.completeDefenseTransit(defenseMessage);
		} else {
			System.out.println("UNKNOWN TYPE: " + o.getClass());
		}

	}
}
