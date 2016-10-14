package hack.core.actor;

import hack.core.actor.messages.MissionMessage;
import hack.core.services.MissionService;

import org.springframework.beans.factory.annotation.Autowired;

import akka.actor.UntypedActor;

public class MissionActor extends UntypedActor {

	@Autowired
	private MissionService missionService;

	@Override
	public void onReceive(Object o) throws Exception {
		//TODO - Need to look at persistence and retrigger research events
		
		if (o instanceof MissionMessage) {
			MissionMessage missionMessage = (MissionMessage) o;
			missionService.completeMission(missionMessage);
		} else {
			System.out.println("UNKNOWN TYPE: " + o.getClass());
		}

	}
}
