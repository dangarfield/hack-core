package hack.core.models;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

public class Logs {

	private List<AttackLog> stealMoneyAttackCooldown;
	private List<ObjectId> takeoverLogs;
	private List<AttackLog> missionLogs;

	public Logs() {
		super();
		this.stealMoneyAttackCooldown = new ArrayList<AttackLog>();
		this.takeoverLogs = new ArrayList<ObjectId>();
		this.missionLogs = new ArrayList<AttackLog>();
	}

	public List<AttackLog> getStealMoneyAttackCooldown() {
		return stealMoneyAttackCooldown;
	}

	public void setStealMoneyAttackCooldown(List<AttackLog> stealMoneyAttackCooldown) {
		this.stealMoneyAttackCooldown = stealMoneyAttackCooldown;
	}

	public List<ObjectId> getTakeoverLogs() {
		return takeoverLogs;
	}

	public void setTakeoverLogs(List<ObjectId> takeoverLogs) {
		this.takeoverLogs = takeoverLogs;
	}

	public List<AttackLog> getMissionLogs() {
		return missionLogs;
	}

	public void setMissionLogs(List<AttackLog> missionLogs) {
		this.missionLogs = missionLogs;
	}

}
