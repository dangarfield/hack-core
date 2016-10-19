package hack.core.dto;

import java.util.List;

import org.bson.types.ObjectId;
import org.jongo.marshall.jackson.oid.MongoId;
import org.jongo.marshall.jackson.oid.MongoObjectId;

public class PlayerDTO {

	@MongoId
	@MongoObjectId
	private ObjectId id;
	private String name;
	private List<String> locationIps;

	public PlayerDTO() {
		super();
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public PlayerDTO(ObjectId id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public List<String> getLocationIps() {
		return locationIps;
	}

	public void setLocationIps(List<String> locationIps) {
		this.locationIps = locationIps;
	}

}
