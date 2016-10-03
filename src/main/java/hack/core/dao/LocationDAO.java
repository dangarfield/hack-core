package hack.core.dao;

import java.util.ArrayList;
import java.util.List;

import hack.core.models.Location;
import hack.core.models.Player;

import org.jongo.Aggregate.ResultsIterator;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocationDAO {

	@Autowired
	private MongoCollection locations;

	public boolean doesIPAlreadyExist(String ip) {
		long count = locations.count("{ip:#}", ip);
		return count > 0 ? true : false;
	}

	public void saveLocation(Location location) {
		locations.save(location);
	}

	public List<Location> getLocationsForEmail(Player player) {
		MongoCursor<Location> locationCursor = locations.find("{player:#}", player.getId()).as(Location.class);
		List<Location> locations = new ArrayList<Location>();
		for (Location location : locationCursor) {
			locations.add(location);
		}
		return locations;
	}

	public List<Location> getRandomLocations(Player player, int no) {
		ResultsIterator<Location> locationCursor = locations.aggregate("{$match:{player:{$ne:#},npc:false}}", player.getId())
				.and("{$sample:{size:#}}", no).as(Location.class);
		List<Location> locations = new ArrayList<Location>();
		for (Location location : locationCursor) {
			locations.add(location);
		}
		return locations;
	}

	public void removeAllData() {
		locations.remove();
	}

	public void incrementAllOfAPlayersLocationsResearchHideLevel(Player player, int noOfLevels) {
		locations.update("{player:#}", player.getId()).multi().with("{$inc:{researchHideLevel:#}}", noOfLevels);
	}
}
