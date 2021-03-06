package hack.core.models;

import java.util.Date;

public class TransitTroop extends Troop {

	private String source;
	private String target;
	private Date arrival;

	public TransitTroop() {
		super();
	}

	public TransitTroop(TroopType type, long noOfTroops, String source, String target, Date arrival) {
		super(type, noOfTroops);
		this.source = source;
		this.target = target;
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

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TransitTroop) {
			TransitTroop o = (TransitTroop) obj;
			if (o.getType().equals(this.getType()) && o.getNoOfTroops() == this.getNoOfTroops() && o.getSource().equals(this.getSource())
					&& o.getTarget().equals(this.getTarget()) && dateEquality(o.getArrival(), this.getArrival())) {
				return true;
			}
		}
		return false;
	}
	
	private boolean dateEquality(Date date, Date date2) {
		if(date == null && date2 == null) {
			return true;
		} else if(date.equals(date2)) {
			return true;
		} else {
			return false;
		}
	}

}
