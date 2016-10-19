package hack.core.models;

import java.util.Date;

import org.bson.types.ObjectId;
import org.jongo.marshall.jackson.oid.MongoId;
import org.jongo.marshall.jackson.oid.MongoObjectId;

public class TopicShort {

	@MongoId
	@MongoObjectId
	private ObjectId id;
	private String syndicateId;

	private String title;
	private Date lastAmended;
	private boolean locked;

	public TopicShort() {
		super();
	}

	public TopicShort(ObjectId id, String syndicateId, String title,
			Date lastAmended, boolean locked) {
		super();
		this.id = id;
		this.syndicateId = syndicateId;
		this.title = title;
		this.lastAmended = lastAmended;
		this.locked = locked;
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getSyndicateId() {
		return syndicateId;
	}

	public void setSyndicateId(String syndicateId) {
		this.syndicateId = syndicateId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getLastAmended() {
		return lastAmended;
	}

	public void setLastAmended(Date lastAmended) {
		this.lastAmended = lastAmended;
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof TopicShort) {
			TopicShort ot = (TopicShort) o;
			if(ot.getId().equals(this.getId())) {
				return true;
			}
		}
		return false;
	}

}
