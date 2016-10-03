package hack.core.models;

public class Troop {

	private TroopType type;
	private int noOfTroops;

	public Troop() {
		super();
	}
	public Troop(TroopType type, int noOfTroops) {
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

	public int getNoOfTroops() {
		return noOfTroops;
	}

	public void setNoOfTroops(int noOfTroops) {
		this.noOfTroops = noOfTroops;
	}

}
