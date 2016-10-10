package hack.core.config;

import org.jongo.Jongo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mongodb.DB;
import com.mongodb.MongoClient;

@Configuration
public class MongoConfig {

	//private static final Logger LOG = Logger.getLogger("MongoConfig");

	@Bean
	public Jongo jongo() {
		@SuppressWarnings("resource")
		MongoClient mc = new MongoClient();
		@SuppressWarnings("deprecation")
		DB db = mc.getDB(getDBName());
		Jongo jongo = new Jongo(db);
		return jongo;
	}

	private String getDBName() {
		return "hack_spring";
	}

}
