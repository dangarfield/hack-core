package hack.core.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

	private List<AttackLog> stealMoneyAttackCooldown;
	private List<AttackLog> takeovers;

	public Player() {
		super();
		this.locationIps = new ArrayList<String>();
		this.stealMoneyAttackCooldown = new ArrayList<AttackLog>();
		this.takeovers = new ArrayList<AttackLog>();
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

	public List<AttackLog> getStealMoneyAttackCooldown() {
		return stealMoneyAttackCooldown;
	}

	public void setStealMoneyAttackCooldown(List<AttackLog> stealMoneyAttackCooldown) {
		this.stealMoneyAttackCooldown = stealMoneyAttackCooldown;
	}

	public List<AttackLog> getTakeovers() {
		return takeovers;
	}

	public void setTakeovers(List<AttackLog> takeovers) {
		this.takeovers = takeovers;
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

	public int currentNoOfResearchInTraining() {
		int no = 0;
		for (Research research : this.researches) {
			no = no + research.getCurrentlyTraining().size();
		}
		return no;
	}

	public AttackLog getRecentAttackForIp(String targetIp) {
		for (AttackLog attackLog : this.stealMoneyAttackCooldown) {
			Date cutOff = new Date(new Date().getTime() - (60 * 60 * 1000));
			if(attackLog.getTargetIp().equals(targetIp) && attackLog.getTime().compareTo(cutOff) > 0 ) {
				return attackLog;
			}
		}
		return null;
	}
}
