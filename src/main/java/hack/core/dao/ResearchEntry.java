package hack.core.dao;

import hack.core.actor.messages.ResearchMessage;
import hack.core.models.TrainingResearch;

public class ResearchEntry {

	private TrainingResearch trainingResearch;
	private ResearchMessage researchMessage;

	public TrainingResearch getTrainingResearch() {
		return trainingResearch;
	}

	public void setTrainingResearch(TrainingResearch trainingResearch) {
		this.trainingResearch = trainingResearch;
	}

	public ResearchMessage getResearchMessage() {
		return researchMessage;
	}

	public void setResearchMessage(ResearchMessage researchMessage) {
		this.researchMessage = researchMessage;
	}

	public ResearchEntry() {
		super();
	}

}
