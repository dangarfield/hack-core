package hack.core.actor.messages;

import org.bson.types.ObjectId;

import hack.core.models.MissionType;

public class MissionMessage {
	private ObjectId playerId;
	private MissionType type;

	public MissionMessage() {
		super();
	}

	public MissionMessage(ObjectId playerId, MissionType type) {
		super();
		this.playerId = playerId;
		this.type = type;
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

}
