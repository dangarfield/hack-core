package hack.core.actor.messages;

import java.util.List;

import org.bson.types.ObjectId;

import hack.core.models.TransitTroop;

public class AttackMessage extends AbstractMessage {
	private ObjectId sourcePlayerId;
	private ObjectId targetPlayerId;
	private List<TransitTroop> attackingTroops;
	private int ceo;

	public AttackMessage() {
		super();
	}

	public AttackMessage(ObjectId sourcePlayerId, ObjectId targetPlayerId, List<TransitTroop> attackingTroops, int ceo) {
		super();
		this.sourcePlayerId = sourcePlayerId;
		this.targetPlayerId = targetPlayerId;
		this.attackingTroops = attackingTroops;
		this.ceo = ceo;
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

	public int getCeo() {
		return ceo;
	}

	public void setCeo(int ceo) {
		this.ceo = ceo;
	}

}
