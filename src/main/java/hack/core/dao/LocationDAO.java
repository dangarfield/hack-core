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

	public Location getLocationFromIp(String ip) {
		return locations.findOne("{_id:#}", ip).as(Location.class);
	}

	public List<MapTileEntry> getMapTile(Player player, int x, int y) {
		List<MapTileEntry> mapTile = new ArrayList<MapTileEntry>();
		
		int xMax = x + 100;
		int yMax = y + 100;
		StringBuilder match = new StringBuilder();
		match.append("{$and: [");
		match.append("{'coord.x': {$gte:#}},");
		match.append("{'coord.x': {$lt:#}},");
		match.append("{'coord.y': {$gte:#}},");
		match.append("{'coord.y': {$lt:#}}");
		match.append("]}");

		// {
		// $and: [
		// {'coord.x': {$gte:0}},
		// {'coord.x': {$lt:100}},
		// {'coord.y': {$gte:0}},
		// {'coord.y': {$lt:100}}
		// ]
		// }

		MongoCursor<Location> res = locations.find(match.toString(), x, xMax, y, yMax).as(Location.class);
		for (Location location : res) {
			mapTile.add(new MapTileEntry(location.getIp(), location.getCoord(), location.getPlayer(), location.isNpc()));
		}
		return mapTile;
	}
}
