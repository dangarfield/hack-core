package hack.core.dto;

public class APIRequestDTO {

	private ActionType action;
	private String type;

	public APIRequestDTO() {
		super();
	}

	public ActionType getAction() {
		return action;
	}

	public void setAction(ActionType action) {
		this.action = action;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
