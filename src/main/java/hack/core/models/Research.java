package hack.core.models;

import java.util.ArrayList;
import java.util.List;

public class Research {

	private ResearchType type;
	private int level;
	private List<TrainingResearch> currentlyTraining;

	public Research() {
		super();
		this.currentlyTraining = new ArrayList<TrainingResearch>();
	}
	
	public Research(ResearchType type, int level) {
		super();
		this.type = type;
		this.level = level;
		this.currentlyTraining = new ArrayList<TrainingResearch>();
	}

	public ResearchType getType() {
		return type;
	}

	public void setType(ResearchType type) {
		this.type = type;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public List<TrainingResearch> getCurrentlyTraining() {
		return currentlyTraining;
	}

	public void setCurrentlyTraining(List<TrainingResearch> currentlyTraining) {
		this.currentlyTraining = currentlyTraining;
	}

	public TrainingResearch getCurrentTrainingByID(String id) {
		for (TrainingResearch trainingResearch : this.currentlyTraining) {
			if(trainingResearch.getId().equals(id)) {
				return trainingResearch;
			}
		}
		return null;
	}
	
	// public enum ResearchType {
	//
	// }
	// private int upgradeSpeed; // Upgrade Speed
	// private int upgradeParallel; // Upgrade Parallel
	//
	// private int defenceBase; // Defence Base (HP)
	// private int defenceHardenFirewall; // Defence harden Firewall
	// private int defenceHardenPhysical; // Defence harden Physical
	// private int defenceHardenData; // Defence harden Data
	//
	// private int attackBonusFirewall; // Attack bonus Firewall
	// private int attackBonusPhysical; // Attack bonus Physical
	// private int attackBonusData; // Attack bonus Data
	//
	// private int moneyReserved; // Money Reserved

}
