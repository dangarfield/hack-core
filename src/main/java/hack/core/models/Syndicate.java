package hack.core.models;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.jongo.marshall.jackson.oid.MongoId;

public class Syndicate {

	@MongoId
	private String id;
	private String name;
	private String description;

	private List<ObjectId> players;
	private List<ObjectId> admins;
	private List<ObjectId> applicants;

	private List<TopicShort> topics;

	public Syndicate() {
		super();
		this.players = new ArrayList<ObjectId>();
		this.admins = new ArrayList<ObjectId>();
		this.applicants = new ArrayList<ObjectId>();
		this.topics = new ArrayList<TopicShort>();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<ObjectId> getPlayers() {
		return players;
	}

	public void setPlayers(List<ObjectId> players) {
		this.players = players;
	}

	public List<ObjectId> getAdmins() {
		return admins;
	}

	public void setAdmins(List<ObjectId> admins) {
		this.admins = admins;
	}

	public List<ObjectId> getApplicants() {
		return applicants;
	}

	public void setApplicants(List<ObjectId> applicants) {
		this.applicants = applicants;
	}

	public List<TopicShort> getTopics() {
		return topics;
	}

	public void setTopics(List<TopicShort> topics) {
		this.topics = topics;
	}

}
