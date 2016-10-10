package hack.core.config;

import java.util.logging.Logger;

import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoCollections {
	
	private static final Logger LOG = Logger.getLogger("MongoCollections");

	@Autowired
	private Jongo jongo;

	@Bean
	public MongoCollection players() {
		LOG.info("Bean created: players");
		return jongo.getCollection("players");
	}

	@Bean
	public MongoCollection locations() {
		LOG.info("Bean created: locations");
		return jongo.getCollection("locations");
	}

	@Bean
	public MongoCollection config() {
		LOG.info("Bean created: config");
		return jongo.getCollection("config");
	}

}
