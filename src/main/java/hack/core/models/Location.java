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
	
	private List<Troop> troopsOwn;
	private List<Troop> troopsOwnReturnTransit;

	private List<Troop> defenceIn;
	private List<Troop> defenceInTransit;
	private List<Troop> defenceOut;
	private List<Troop> defenceOutTransit;

	private List<Troop> attackIn;
	private List<Troop> attackInTransit;
	private List<Troop> attackOut;
	private List<Troop> attackOutTransit;

	public Location() {
		super();
		
		this.troopsOwn = new ArrayList<Troop>();
		this.troopsOwnReturnTransit = new ArrayList<Troop>();
		
		this.defenceIn = new ArrayList<Troop>();
		this.defenceInTransit = new ArrayList<Troop>();
		this.defenceOut = new ArrayList<Troop>();
		this.defenceOutTransit = new ArrayList<Troop>();
		
		this.attackIn = new ArrayList<Troop>();
		this.attackInTransit = new ArrayList<Troop>();
		this.attackOut = new ArrayList<Troop>();
		this.attackOutTransit = new ArrayList<Troop>();
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

	public List<Troop> getTroopsOwn() {
		return troopsOwn;
	}

	public void setTroopsOwn(List<Troop> troopsOwn) {
		this.troopsOwn = troopsOwn;
	}

	public List<Troop> getTroopsOwnReturnTransit() {
		return troopsOwnReturnTransit;
	}

	public void setTroopsOwnReturnTransit(List<Troop> troopsOwnReturnTransit) {
		this.troopsOwnReturnTransit = troopsOwnReturnTransit;
	}

	public List<Troop> getDefenceIn() {
		return defenceIn;
	}

	public void setDefenceIn(List<Troop> defenceIn) {
		this.defenceIn = defenceIn;
	}

	public List<Troop> getDefenceInTransit() {
		return defenceInTransit;
	}

	public void setDefenceInTransit(List<Troop> defenceInTransit) {
		this.defenceInTransit = defenceInTransit;
	}

	public List<Troop> getDefenceOut() {
		return defenceOut;
	}

	public void setDefenceOut(List<Troop> defenceOut) {
		this.defenceOut = defenceOut;
	}

	public List<Troop> getDefenceOutTransit() {
		return defenceOutTransit;
	}

	public void setDefenceOutTransit(List<Troop> defenceOutTransit) {
		this.defenceOutTransit = defenceOutTransit;
	}

	public List<Troop> getAttackIn() {
		return attackIn;
	}

	public void setAttackIn(List<Troop> attackIn) {
		this.attackIn = attackIn;
	}

	public List<Troop> getAttackInTransit() {
		return attackInTransit;
	}

	public void setAttackInTransit(List<Troop> attackInTransit) {
		this.attackInTransit = attackInTransit;
	}

	public List<Troop> getAttackOut() {
		return attackOut;
	}

	public void setAttackOut(List<Troop> attackOut) {
		this.attackOut = attackOut;
	}

	public List<Troop> getAttackOutTransit() {
		return attackOutTransit;
	}

	public void setAttackOutTransit(List<Troop> attackOutTransit) {
		this.attackOutTransit = attackOutTransit;
	}

}
