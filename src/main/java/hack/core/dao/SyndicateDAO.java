package hack.core.dao;

import hack.core.models.Syndicate;
import hack.core.models.Topic;

import org.jongo.MongoCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SyndicateDAO {

	@Autowired
	private MongoCollection syndicates;

	public void saveSyndicate(Syndicate syndicate) {
		syndicates.save(syndicate);
	}

	public void removeAllData() {
		syndicates.remove();
	}

	public Syndicate getSyndicateById(String id) {
		return syndicates.findOne("{_id:#}",id).as(Syndicate.class);
	}

	public void remove(String id) {
		syndicates.remove("{_id:#}",id);
	}

	public void updateTopicShort(Topic topic) {
		syndicates.update("{\"topics._id\":#}",topic.getId()).with("{$set: {\"topics.$.lastAmended\":#} }",topic.getLastAmended());
	}
}
