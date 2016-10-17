package hack.core.dao;

import java.util.ArrayList;
import java.util.List;

import hack.core.actor.messages.RecruitmentMessage;
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
		MongoCursor<Location> locationCursor = locations.find("{player:#}",
				player.getId()).as(Location.class);
		List<Location> locations = new ArrayList<Location>();
		for (Location location : locationCursor) {
			locations.add(location);
		}
		return locations;
	}

	public List<Location> getRandomLocations(Player player, int no) {
		ResultsIterator<Location> locationCursor = locations
				.aggregate("{$match:{player:{$ne:#},npc:false}}",
						player.getId()).and("{$sample:{size:#}}", no)
				.as(Location.class);
		List<Location> locations = new ArrayList<Location>();
		for (Location location : locationCursor) {
			locations.add(location);
		}
		return locations;
	}

	public void removeAllData() {
		locations.remove();
	}

	public void incrementAllOfAPlayersLocationsResearchHideLevel(Player player,
			int noOfLevels) {
		locations.update("{player:#}", player.getId()).multi()
				.with("{$inc:{researchHideLevel:#}}", noOfLevels);
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

		MongoCursor<Location> res = locations.find(match.toString(), x, xMax,
				y, yMax).as(Location.class);
		for (Location location : res) {
			mapTile.add(new MapTileEntry(location.getIp(), location.getCoord(),
					location.getPlayer(), location.isNpc()));
		}
		return mapTile;
	}

	public List<RecruitmentMessage> getRecruitingForRestart() {
		// TODO - Do this properly
		List<RecruitmentMessage> messages = new ArrayList<RecruitmentMessage>();
		ResultsIterator<RecruitmentMessage> agg = locations
				.aggregate("{$match : {\"recruiting\": {$not: {$size:0} } }}")
				.and("{$unwind : \"$recruiting\" }")
				.and("{$project: {_id:0,ip:\"$_id\",type:\"$recruiting.type\",recruitmentTime:\"$recruiting.recruitmentTime\"}}")
				.as(RecruitmentMessage.class);
		for (RecruitmentMessage message : agg) {
			messages.add(message);
		}
		return messages;
	}

	public List<TakeoverTroopsEntry> getTransitTroopsAttackForRestart() {
		return getTransitTroopsForRestartByType("attackTransitOut");
	}

	public List<TakeoverTroopsEntry> getTransitTroopsDefenseForRestart() {
		return getTransitTroopsForRestartByType("defenseTransitOut");
	}

	public List<TakeoverTroopsEntry> getTransitTroopsReturningForRestart() {
		return getTransitTroopsForRestartByType("returning");
	}

	private List<TakeoverTroopsEntry> getTransitTroopsForRestartByType(
			String type) {
		List<TakeoverTroopsEntry> listOfLists = new ArrayList<TakeoverTroopsEntry>();

		ResultsIterator<TakeoverTroopsEntry> agg = locations
				.aggregate("{$match : {\"" + type + "\": {$not: {$size:0} } }}")
				.and("{$unwind : \"$" + type + "\" }")
				.and("{$group: {_id : {\"source\":\"$" + type
						+ ".source\", \"target\":\"$" + type
						+ ".target\", \"arrival\":\"$" + type
						+ ".arrival\"},troops : {$push:\"$" + type + "\"}}}")
				.and("{$project: {_id:0,troops:1,ceo:1}}")
				.as(TakeoverTroopsEntry.class);
		for (TakeoverTroopsEntry entry : agg) {
			listOfLists.add(entry);
		}
		return listOfLists;
	}

	public Location getRandomLocationForPlayer(Player player) {
		ResultsIterator<Location> locationCursor = locations
				.aggregate("{$match:{player:#}}", player.getId())
				.and("{$sample:{size:#}}", 1).as(Location.class);
		if (locationCursor.hasNext()) {
			return locationCursor.next();
		} else {
			return null;
		}
	}

}
