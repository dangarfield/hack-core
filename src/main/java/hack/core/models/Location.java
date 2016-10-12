package hack.core.models;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.jongo.marshall.jackson.oid.MongoId;

public class Location {

	@MongoId
	private String ip;
	private ObjectId player;
	private boolean isNpc;
	private Coord coord;

	private int researchHideLevel;

	private List<Troop> defense;
	private List<TransitTroop> attackIn;
	private List<TransitTroop> attackOut;
	private List<TrainingTroop> recruiting;

	public Location() {
		super();
		this.defense = new ArrayList<Troop>();
		this.attackIn = new ArrayList<TransitTroop>();
		this.attackOut = new ArrayList<TransitTroop>();
		this.recruiting = new ArrayList<TrainingTroop>();
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public ObjectId getPlayer() {
		return player;
	}

	public void setPlayer(ObjectId player) {
		this.player = player;
	}

	public boolean isNpc() {
		return isNpc;
	}

	public void setNpc(boolean isNpc) {
		this.isNpc = isNpc;
	}

	public Coord getCoord() {
		return coord;
	}

	public void setCoord(Coord coord) {
		this.coord = coord;
	}

	public int getResearchHideLevel() {
		return researchHideLevel;
	}

	public void setResearchHideLevel(int researchHideLevel) {
		this.researchHideLevel = researchHideLevel;
	}

	public List<Troop> getDefense() {
		return defense;
	}

	public void setDefense(List<Troop> defense) {
		this.defense = defense;
	}

	public List<TransitTroop> getAttackIn() {
		return attackIn;
	}

	public void setAttackIn(List<TransitTroop> attackIn) {
		this.attackIn = attackIn;
	}

	public List<TransitTroop> getAttackOut() {
		return attackOut;
	}

	public void setAttackOut(List<TransitTroop> attackOut) {
		this.attackOut = attackOut;
	}

	public List<TrainingTroop> getRecruiting() {
		return recruiting;
	}

	public void setRecruiting(List<TrainingTroop> recruiting) {
		this.recruiting = recruiting;
	}

}
