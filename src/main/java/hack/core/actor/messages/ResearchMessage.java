package hack.core.actor.messages;

import hack.core.models.ResearchType;

public class ResearchMessage extends AbstractMessage {
	private String playerEmail;
	private ResearchType type;
	private String id;

	public ResearchMessage() {
		super();
	}

	public ResearchMessage(String playerEmail, ResearchType type, String id) {
		super();
		this.playerEmail = playerEmail;
		this.type = type;
		this.id = id;
	}

	public String getPlayerEmail() {
		return playerEmail;
	}

	public void setPlayerEmail(String playerEmail) {
		this.playerEmail = playerEmail;
	}

	public ResearchType getType() {
		return type;
	}

	public void setType(ResearchType type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
