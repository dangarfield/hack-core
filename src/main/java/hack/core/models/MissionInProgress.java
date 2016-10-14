package hack.core.models;

import java.util.Date;

public class MissionInProgress {

	private MissionType type;
	private Date startTime;
	private Date endTime;

	public MissionInProgress() {
		super();
	}

	public MissionInProgress(MissionType type, Date startTime, Date endTime) {
		super();
		this.type = type;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public MissionType getType() {
		return type;
	}

	public void setType(MissionType type) {
		this.type = type;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

}
