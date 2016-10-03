package hack.core.models;

import hack.core.dto.APIResultType;

import java.util.Date;

public class AttackLog {

	private AttackType type;
	private Date time;
	private String sourceIp;
	private String targetIp;
	private APIResultType result;
	private String message;

	public AttackLog() {
		super();
	}

	public AttackLog(AttackType type, Date time, String sourceIp, String targetIp, APIResultType result, String message) {
		super();
		this.type = type;
		this.time = time;
		this.sourceIp = sourceIp;
		this.targetIp = targetIp;
		this.result = result;
		this.message = message;
	}

	public AttackType getType() {
		return type;
	}

	public void setType(AttackType type) {
		this.type = type;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getSourceIp() {
		return sourceIp;
	}

	public void setSourceIp(String sourceIp) {
		this.sourceIp = sourceIp;
	}

	public String getTargetIp() {
		return targetIp;
	}

	public void setTargetIp(String targetIp) {
		this.targetIp = targetIp;
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
