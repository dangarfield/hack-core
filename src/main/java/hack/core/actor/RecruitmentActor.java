package hack.core.actor;

import hack.core.actor.messages.RecruitmentMessage;
import hack.core.services.RecruitmentService;

import org.springframework.beans.factory.annotation.Autowired;

import akka.actor.UntypedActor;

public class RecruitmentActor extends UntypedActor {

	@Autowired
	private RecruitmentService recruitmentService;

	@Override
	public void onReceive(Object o) throws Exception {
		if (o instanceof RecruitmentMessage) {
			RecruitmentMessage recruitmentMessage = (RecruitmentMessage) o;
			recruitmentService.completeRecruitment(recruitmentMessage);
		} else {
			System.out.println("UNKNOWN TYPE: " + o.getClass());
		}

	}
}
