package hack.core.models;

import java.util.Date;

public class MissionLog {

	private MissionType type;
	private Date time;
	private String message;

	public MissionLog() {
		super();
	}

	public MissionLog(MissionType type, Date time, String message) {
		super();
		this.type = type;
		this.time = time;
		this.message = message;
	}

	public MissionType getType() {
		return type;
	}

	public void setType(MissionType type) {
		this.type = type;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
