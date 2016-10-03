package hack.core.dto;

public class APIResultDTO {

	private APIResultType result;
	private String message;

	public APIResultDTO() {
		super();
	}

	public APIResultDTO(APIResultType result, String message) {
		super();
		this.result = result;
		this.message = message;
	}

	public APIResultType getResult() {
		return result;
	}

	public void setResult(APIResultType result) {
		this.result = result;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
