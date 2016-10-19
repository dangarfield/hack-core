package hack.core.dao;

import hack.core.models.Topic;

import org.bson.types.ObjectId;
import org.jongo.MongoCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TopicDAO {

	@Autowired
	private MongoCollection topics;

	public void saveTopic(Topic topic) {
		topics.save(topic);
	}

	public void removeAllData() {
		topics.remove();
	}

	public void removeAllSyndicatesPosts(String id) {
		topics.remove("{syndicateId:#}", id);
	}

	public Topic getTopicById(ObjectId topicId) {
		return topics.findOne("{_id:#}",topicId).as(Topic.class);
	}

	public void removeTopic(Topic topic) {
		topics.remove("{_id:#}", topic.getId());
	}
}
