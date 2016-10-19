package hack.core.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;

public class Topic extends TopicShort {

	private List<TopicPost> posts;

	public Topic() {
		super();
		this.posts = new ArrayList<TopicPost>();
	}
	public Topic(ObjectId id, String syndicateId, String title,
			Date lastAmended, boolean locked) {
		super(id, syndicateId, title, lastAmended, locked);
		this.posts = new ArrayList<TopicPost>();
	}

	public List<TopicPost> getPosts() {
		return posts;
	}

	public void setPosts(List<TopicPost> posts) {
		this.posts = posts;
	}
	
	public TopicShort toTopicShort() {
		return new TopicShort(this.getId(), this.getSyndicateId(), this.getTitle(), this.getLastAmended(), this.isLocked());
	}

	@Override
	public boolean equals(Object o) {
		if(o instanceof Topic) {
			Topic ot = (Topic) o;
			if(ot.getId().equals(this.getId())) {
				return true;
			}
		}
		return false;
	}
}
