package hack.core.actor.messages;

import java.util.List;

import org.bson.types.ObjectId;

import hack.core.models.TransitTroop;

public class AttackMessage {
	private ObjectId sourcePlayerId;
	private ObjectId targetPlayerId;
	private List<TransitTroop> attackingTroops;

	public AttackMessage() {
		super();
	}

	public AttackMessage(ObjectId sourcePlayerId, ObjectId targetPlayerId, List<TransitTroop> attackingTroops) {
		super();
		this.sourcePlayerId = sourcePlayerId;
		this.targetPlayerId = targetPlayerId;
		this.attackingTroops = attackingTroops;
	}

	public ObjectId getSourcePlayerId() {
		return sourcePlayerId;
	}

	public void setSourcePlayerId(ObjectId sourcePlayerId) {
		this.sourcePlayerId = sourcePlayerId;
	}

	public ObjectId getTargetPlayerId() {
		return targetPlayerId;
	}

	public void setTargetPlayerId(ObjectId targetPlayerId) {
		this.targetPlayerId = targetPlayerId;
	}

	public List<TransitTroop> getAttackingTroops() {
		return attackingTroops;
	}

	public void setAttackingTroops(List<TransitTroop> attackingTroops) {
		this.attackingTroops = attackingTroops;
	}

}
