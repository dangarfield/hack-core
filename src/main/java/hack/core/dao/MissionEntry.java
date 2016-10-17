package hack.core.dao;

import java.util.Date;

import org.bson.types.ObjectId;

import hack.core.models.MissionType;

public class MissionEntry {

	private ObjectId playerId;
	private MissionType type;
	private Date completionTime;

	public MissionEntry() {
		super();
	}

	public ObjectId getPlayerId() {
		return playerId;
	}

	public void setPlayerId(ObjectId playerId) {
		this.playerId = playerId;
	}

	public MissionType getType() {
		return type;
	}

	public void setType(MissionType type) {
		this.type = type;
	}

	public Date getCompletionTime() {
		return completionTime;
	}

	public void setCompletionTime(Date completionTime) {
		this.completionTime = completionTime;
	}

}
