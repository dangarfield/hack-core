package hack.core.dao;

import java.util.List;

import hack.core.models.TransitTroop;

public class TakeoverTroopsEntry {

	private List<TransitTroop> troops;

	public TakeoverTroopsEntry() {
		super();
	}

	public List<TransitTroop> getTroops() {
		return troops;
	}

	public void setTroops(List<TransitTroop> troops) {
		this.troops = troops;
	}

}
