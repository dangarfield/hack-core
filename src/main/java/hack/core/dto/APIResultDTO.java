package hack.core.dto;

public class APIResultDTO {

	private APIResultType result;
	private Object message;

	public APIResultDTO() {
		super();
	}

	public APIResultDTO(APIResultType result, Object message) {
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

	public Object getMessage() {
		return message;
	}

	public void setMessage(Object message) {
		this.message = message;
	}

}
