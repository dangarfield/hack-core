package hack.core.models;

public class Troop {

	private TroopType type;
	private long noOfTroops;

	public Troop() {
		super();
	}
	public Troop(TroopType type, long noOfTroops) {
		super();
		this.type = type;
		this.noOfTroops = noOfTroops;
	}

	public TroopType getType() {
		return type;
	}

	public void setType(TroopType type) {
		this.type = type;
	}

	public long getNoOfTroops() {
		return noOfTroops;
	}

	public void setNoOfTroops(long noOfTroops) {
		this.noOfTroops = noOfTroops;
	}

}
