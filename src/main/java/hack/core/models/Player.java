package hack.core.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.bson.types.ObjectId;
import org.jongo.marshall.jackson.oid.MongoId;
import org.jongo.marshall.jackson.oid.MongoObjectId;

public class Player {

	@MongoId
	@MongoObjectId
	private ObjectId id;
	private String name;
	private String email;
	private String password;
	private boolean isNpc;

	private Set<Research> researches;
	private List<String> locationIps;

	private long money;

	private Logs logs;
	private int ceoCount;

	private List<MissionInProgress> missions;
	
	public Player() {
		super();
		this.locationIps = new ArrayList<String>();
		this.researches = new HashSet<Research>();
		this.missions = new ArrayList<MissionInProgress>();
		this.logs = new Logs();
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isNpc() {
		return isNpc;
	}

	public void setNpc(boolean isNpc) {
		this.isNpc = isNpc;
	}

	public Set<Research> getResearches() {
		return researches;
	}

	public void setResearches(Set<Research> researches) {
		this.researches = researches;
	}

	public List<String> getLocationIps() {
		return locationIps;
	}

	public void setLocationIps(List<String> locationIps) {
		this.locationIps = locationIps;
	}

	public long getMoney() {
		return money;
	}

	public void setMoney(long money) {
		this.money = money;
	}


	public Logs getLogs() {
		return logs;
	}

	public void setLogs(Logs logs) {
		this.logs = logs;
	}

	public int getCeoCount() {
		return ceoCount;
	}

	public void setCeoCount(int ceoCount) {
		this.ceoCount = ceoCount;
	}

	public List<MissionInProgress> getMissions() {
		return missions;
	}

	public void setMissions(List<MissionInProgress> missions) {
		this.missions = missions;
	}

	public Research researchOfType(String type) {
		try {
			ResearchType resType = ResearchType.valueOf(type);
			return researchOfType(resType);
		} catch (IllegalArgumentException e) {
			return null;
		}
	}

	public Research researchOfType(ResearchType type) {
		for (Research research : this.researches) {
			if (research.getType().equals(type)) {
				return research;
			}
		}
		return null;
	}

	public MissionInProgress missionOfType(MissionType type) {
		Optional<MissionInProgress> promise = this.missions.stream().filter(m -> m.getType().equals(type)).findFirst();
		if(promise.isPresent()) {
			return promise.get();
		}
		return null;
	}
	public int currentNoOfResearchInTraining() {
		int no = 0;
		for (Research research : this.researches) {
			no = no + research.getCurrentlyTraining().size();
		}
		return no;
	}

	public AttackLog getRecentAttackForIp(String targetIp) {
		for (AttackLog attackLog : this.getLogs().getStealMoneyAttackCooldown()) {
			Date cutOff = new Date(new Date().getTime() - (60 * 60 * 1000));
			if (attackLog.getTargetIp().equals(targetIp) && attackLog.getTime().compareTo(cutOff) > 0) {
				return attackLog;
			}
		}
		return null;
	}
}
