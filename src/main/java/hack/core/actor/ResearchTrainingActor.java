package hack.core.actor;

import hack.core.actor.messages.ResearchMessage;
import hack.core.services.ResearchService;

import org.springframework.beans.factory.annotation.Autowired;

import akka.actor.UntypedActor;

public class ResearchTrainingActor extends UntypedActor {

	@Autowired
	private ResearchService researchService;

	@Override
	public void onReceive(Object o) throws Exception {
		//TODO - Need to look at persistence and retrigger research events
		
		if (o instanceof ResearchMessage) {
			ResearchMessage researchMessage = (ResearchMessage) o;
			researchService.completeTraining(researchMessage);
		} else {
			System.out.println("UNKNOWN TYPE: " + o.getClass());
		}

	}
}
