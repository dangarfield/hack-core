package hack.core.models;

public class TrainingTroop extends Troop {

	private long recruitmentTime;

	public TrainingTroop() {
		super();
	}

	public TrainingTroop(TroopType type, long noOfTroopsDate, long recruitmentTime) {
		super(type, noOfTroopsDate);
		this.recruitmentTime = recruitmentTime;
	}

	
	public long getRecruitmentTime() {
		return recruitmentTime;
	}

	public void setRecruitmentTime(long recruitmentTime) {
		this.recruitmentTime = recruitmentTime;
	}

	@Override
	public boolean equals(Object obj) {
	    if(obj instanceof TrainingTroop) {
	    	TrainingTroop o = (TrainingTroop) obj;
	        if(o.getType().equals(this.getType()) && o.getNoOfTroops() == this.getNoOfTroops() && o.getRecruitmentTime() == this.getRecruitmentTime()) {
	        	return true;
	        }
	    }
	    return false;
	}

}