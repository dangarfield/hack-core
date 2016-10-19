package hack.core.models;

import org.bson.types.ObjectId;

public class TopicPost {

	private String playerName;
	private ObjectId playerId;
	private String content;

	public TopicPost() {
		super();
	}

	public TopicPost(String playerName, ObjectId playerId, String content) {
		super();
		this.playerName = playerName;
		this.playerId = playerId;
		this.content = content;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public ObjectId getPlayerId() {
		return playerId;
	}

	public void setPlayerId(ObjectId playerId) {
		this.playerId = playerId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
