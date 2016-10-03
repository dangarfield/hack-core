package hack.core.models;

import java.util.Date;

public class TrainingResearch {

	private String id;
	private Date startTime;
	private Date endTime;

	public TrainingResearch() {
		super();
	}

	public TrainingResearch(String id, Date startTime, Date endTime) {
		super();
		this.id = id;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
