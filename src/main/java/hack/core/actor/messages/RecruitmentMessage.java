package hack.core.actor.messages;

import hack.core.models.TroopType;

public class RecruitmentMessage extends AbstractMessage {
	private String ip;
	private TroopType type;
	private long recruitmentTime;

	public RecruitmentMessage() {
		super();
	}

	public RecruitmentMessage(String ip, TroopType type, long recruitmentTime) {
		super();
		this.ip = ip;
		this.type = type;
		this.recruitmentTime = recruitmentTime;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public TroopType getType() {
		return type;
	}

	public void setType(TroopType type) {
		this.type = type;
	}

	public long getRecruitmentTime() {
		return recruitmentTime;
	}

	public void setRecruitmentTime(long recruitmentTime) {
		this.recruitmentTime = recruitmentTime;
	}
}
