package hack.core.dao;

import java.util.List;

import hack.core.models.TransitTroop;

public class TakeoverTroopsEntry {

	private List<TransitTroop> troops;
	private int ceo;
	
	public TakeoverTroopsEntry() {
		super();
	}

	public List<TransitTroop> getTroops() {
		return troops;
	}

	public void setTroops(List<TransitTroop> troops) {
		this.troops = troops;
	}

	public int getCeo() {
		return ceo;
	}

	public void setCeo(int ceo) {
		this.ceo = ceo;
	}

}
