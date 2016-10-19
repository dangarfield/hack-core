package hack.core.models;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.jongo.marshall.jackson.oid.MongoId;

public class Location {

	@MongoId
	private String ip;
	private ObjectId player;
	private String syndicateId;
	private String syndicateName;
	private boolean isNpc;
	private Coord coord;

	private int researchHideLevel;

	private List<Troop> defense;
	private List<TransitTroop> defenseIn;
	private List<TransitTroop> defenseOut;
	private List<TransitTroop> defenseTransitIn;
	private List<TransitTroop> defenseTransitOut;
	private List<TransitTroop> attackTransitIn;
	private List<TransitTroop> attackTransitOut;
	private List<TransitTroop> returning;
	private List<TrainingTroop> recruiting;

	public Location() {
		super();
		this.defense = new ArrayList<Troop>();
		this.defenseIn = new ArrayList<TransitTroop>();
		this.defenseOut = new ArrayList<TransitTroop>();
		this.defenseTransitIn = new ArrayList<TransitTroop>();
		this.defenseTransitOut = new ArrayList<TransitTroop>();
		this.attackTransitIn = new ArrayList<TransitTroop>();
		this.attackTransitOut = new ArrayList<TransitTroop>();
		this.returning = new ArrayList<TransitTroop>();
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

	public String getSyndicateId() {
		return syndicateId;
	}

	public void setSyndicateId(String syndicateId) {
		this.syndicateId = syndicateId;
	}

	public String getSyndicateName() {
		return syndicateName;
	}

	public void setSyndicateName(String syndicateName) {
		this.syndicateName = syndicateName;
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

	public List<TransitTroop> getDefenseIn() {
		return defenseIn;
	}

	public void setDefenseIn(List<TransitTroop> defenseIn) {
		this.defenseIn = defenseIn;
	}

	public List<TransitTroop> getDefenseOut() {
		return defenseOut;
	}

	public void setDefenseOut(List<TransitTroop> defenseOut) {
		this.defenseOut = defenseOut;
	}

	public List<TransitTroop> getDefenseTransitIn() {
		return defenseTransitIn;
	}

	public void setDefenseTransitIn(List<TransitTroop> defenseTransitIn) {
		this.defenseTransitIn = defenseTransitIn;
	}

	public List<TransitTroop> getDefenseTransitOut() {
		return defenseTransitOut;
	}

	public void setDefenseTransitOut(List<TransitTroop> defenseTransitOut) {
		this.defenseTransitOut = defenseTransitOut;
	}

	public List<TransitTroop> getAttackTransitIn() {
		return attackTransitIn;
	}

	public void setAttackTransitIn(List<TransitTroop> attackTransitIn) {
		this.attackTransitIn = attackTransitIn;
	}

	public List<TransitTroop> getAttackTransitOut() {
		return attackTransitOut;
	}

	public void setAttackTransitOut(List<TransitTroop> attackTransitOut) {
		this.attackTransitOut = attackTransitOut;
	}

	public List<TransitTroop> getReturning() {
		return returning;
	}

	public void setReturning(List<TransitTroop> returning) {
		this.returning = returning;
	}

	public List<TrainingTroop> getRecruiting() {
		return recruiting;
	}

	public void setRecruiting(List<TrainingTroop> recruiting) {
		this.recruiting = recruiting;
	}

}
