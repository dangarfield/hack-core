package hack.core.models;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

public class Logs {

	private List<AttackLog> stealMoneyAttackCooldown;
	private List<ObjectId> takeoverLogs;
	private List<MissionLog> missionLogs;

	public Logs() {
		super();
		this.stealMoneyAttackCooldown = new ArrayList<AttackLog>();
		this.takeoverLogs = new ArrayList<ObjectId>();
		this.missionLogs = new ArrayList<MissionLog>();
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

	public List<MissionLog> getMissionLogs() {
		return missionLogs;
	}

	public void setMissionLogs(List<MissionLog> missionLogs) {
		this.missionLogs = missionLogs;
	}

}
