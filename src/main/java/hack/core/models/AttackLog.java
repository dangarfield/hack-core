package hack.core.models;


import java.util.Date;

public class AttackLog {

	private AttackType type;
	private Date time;
	private String sourceIp;
	private String targetIp;
	private boolean success;
	private String message;

	public AttackLog() {
		super();
	}

	public AttackLog(AttackType type, Date time, String sourceIp, String targetIp, boolean success, String message) {
		super();
		this.type = type;
		this.time = time;
		this.sourceIp = sourceIp;
		this.targetIp = targetIp;
		this.success = success;
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

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
