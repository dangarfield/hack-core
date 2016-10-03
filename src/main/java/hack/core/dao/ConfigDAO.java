package hack.core.dao;

import hack.core.models.Coord;

import org.jongo.MongoCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfigDAO {

	@Autowired
	private MongoCollection config;
	
	public Coord getNewestCoord() {
		return getConfig().getNewestCoord();
	}

	public synchronized void saveNewestCoord(Coord coord) {
		Config c = getConfig();
		c.setNewestCoord(coord);
		config.save(c);
	}

	public Config getConfig() {
		Config c = config.findOne("{_id:\"config\"}").as(Config.class);
		if(c == null) {
			createConfig();
			return getConfig();
		}
		return c;
	}

	private void createConfig() {
		Config c = new Config();
		c.setId("config");
		c.setNewestCoord(new Coord(0, 0));
		config.save(c);
	}
}
