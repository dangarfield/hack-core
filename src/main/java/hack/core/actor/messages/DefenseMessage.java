package hack.core.actor.messages;

import java.util.List;

import hack.core.models.TransitTroop;

public class DefenseMessage extends AbstractMessage {
	
	private List<TransitTroop> defendingTroops;

	public DefenseMessage() {
		super();
	}

	public DefenseMessage(List<TransitTroop> defendingTroops) {
		super();
		this.defendingTroops = defendingTroops;
	}

	public List<TransitTroop> getDefendingTroops() {
		return defendingTroops;
	}

	public void setDefendingTroops(List<TransitTroop> defendingTroops) {
		this.defendingTroops = defendingTroops;
	}

}
