package hack.core.dao;

import org.jongo.marshall.jackson.oid.MongoId;

import hack.core.models.Coord;

public class Config {
	
	@MongoId
	private String id;
	private Coord newestCoord;

	public Config() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Coord getNewestCoord() {
		return newestCoord;
	}

	public void setNewestCoord(Coord newestCoord) {
		this.newestCoord = newestCoord;
	}
}
