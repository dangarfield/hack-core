package hack.core.models;


import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;

public class BattleReport {

	private ObjectId _id;
	private Date time;
	private int attackVictory;
	private int defenseVictory;
	private BattleReportWinner winner;
	private boolean takeoverComplete;
	private String sourceIp;
	private String targetIp;
	
	private List<Troop> defense;
	private List<Troop> defenseRemaining;
	private List<Troop> defenseKilled;
	private List<TransitTroop> defenseIn;
	private List<TransitTroop> defenseInRemaining;
	private List<TransitTroop> defenseInKilled;
	private List<TransitTroop> attackIn;
	private List<TransitTroop> attackInRemaining;
	private List<TransitTroop> attackInKilled;
	
	private String message;

	public BattleReport() {
		super();
	}

	public BattleReport(Date time, int attackVictory, int defenseVictory, BattleReportWinner winner, boolean takeoverComplete, String sourceIp,
			String targetIp, List<Troop> defense, List<Troop> defenseRemaining, List<Troop> defenseKilled, List<TransitTroop> defenseIn,
			List<TransitTroop> defenseInRemaining, List<TransitTroop> defenseInKilled, List<TransitTroop> attackIn,
			List<TransitTroop> attackInRemaining, List<TransitTroop> attackInKilled, String message) {
		super();
		this._id = new ObjectId();
		this.time = time;
		this.attackVictory = attackVictory;
		this.defenseVictory = defenseVictory;
		this.winner = winner;
		this.takeoverComplete = takeoverComplete;
		this.sourceIp = sourceIp;
		this.targetIp = targetIp;
		this.defense = defense;
		this.defenseRemaining = defenseRemaining;
		this.defenseKilled = defenseKilled;
		this.defenseIn = defenseIn;
		this.defenseInRemaining = defenseInRemaining;
		this.defenseInKilled = defenseInKilled;
		this.attackIn = attackIn;
		this.attackInRemaining = attackInRemaining;
		this.attackInKilled = attackInKilled;
		this.message = message;
	}

	

	public ObjectId get_id() {
		return _id;
	}

	public void set_id(ObjectId _id) {
		this._id = _id;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public int getAttackVictory() {
		return attackVictory;
	}

	public void setAttackVictory(int attackVictory) {
		this.attackVictory = attackVictory;
	}

	public int getDefenseVictory() {
		return defenseVictory;
	}

	public void setDefenseVictory(int defenseVictory) {
		this.defenseVictory = defenseVictory;
	}

	public BattleReportWinner getWinner() {
		return winner;
	}

	public void setWinner(BattleReportWinner winner) {
		this.winner = winner;
	}

	public boolean isTakeoverComplete() {
		return takeoverComplete;
	}

	public void setTakeoverComplete(boolean takeoverComplete) {
		this.takeoverComplete = takeoverComplete;
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

	public List<Troop> getDefense() {
		return defense;
	}

	public void setDefense(List<Troop> defense) {
		this.defense = defense;
	}

	public List<Troop> getDefenseRemaining() {
		return defenseRemaining;
	}

	public void setDefenseRemaining(List<Troop> defenseRemaining) {
		this.defenseRemaining = defenseRemaining;
	}

	public List<Troop> getDefenseKilled() {
		return defenseKilled;
	}

	public void setDefenseKilled(List<Troop> defenseKilled) {
		this.defenseKilled = defenseKilled;
	}

	public List<TransitTroop> getDefenseIn() {
		return defenseIn;
	}

	public void setDefenseIn(List<TransitTroop> defenseIn) {
		this.defenseIn = defenseIn;
	}

	public List<TransitTroop> getDefenseInRemaining() {
		return defenseInRemaining;
	}

	public void setDefenseInRemaining(List<TransitTroop> defenseInRemaining) {
		this.defenseInRemaining = defenseInRemaining;
	}

	public List<TransitTroop> getDefenseInKilled() {
		return defenseInKilled;
	}

	public void setDefenseInKilled(List<TransitTroop> defenseInKilled) {
		this.defenseInKilled = defenseInKilled;
	}

	public List<TransitTroop> getAttackIn() {
		return attackIn;
	}

	public void setAttackIn(List<TransitTroop> attackIn) {
		this.attackIn = attackIn;
	}

	public List<TransitTroop> getAttackInRemaining() {
		return attackInRemaining;
	}

	public void setAttackInRemaining(List<TransitTroop> attackInRemaining) {
		this.attackInRemaining = attackInRemaining;
	}

	public List<TransitTroop> getAttackInKilled() {
		return attackInKilled;
	}

	public void setAttackInKilled(List<TransitTroop> attackInKilled) {
		this.attackInKilled = attackInKilled;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
