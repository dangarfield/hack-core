package hack.core.models;

import java.util.Date;

public class TransitTroop extends Troop {

	private String source;
	private String target;
	private Date arrival;

	public TransitTroop() {
		super();
	}

	public TransitTroop(TroopType type, int noOfTroopsDate, Date arrival) {
		super(type, noOfTroopsDate);
		this.arrival = arrival;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public Date getArrival() {
		return arrival;
	}

	public void setArrival(Date arrival) {
		this.arrival = arrival;
	}

}
