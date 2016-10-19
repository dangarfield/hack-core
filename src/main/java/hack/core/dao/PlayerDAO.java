package hack.core.dao;

import java.util.ArrayList;
import java.util.List;

import hack.core.dto.PlayerDTO;
import hack.core.models.Player;

import org.bson.types.ObjectId;
import org.jongo.MongoCollection;
import org.jongo.Aggregate.ResultsIterator;
import org.jongo.MongoCursor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mongodb.WriteResult;

@Service
public class PlayerDAO {

	@Autowired
	private MongoCollection players;

	public boolean doesPlayerAlreadyExist(String email) {
		return players.count("{email:#}",email) > 0 ? true : false;
	}

	public Player getUserByEmail(String email) {
		return players.findOne("{email:#}",email).as(Player.class);
	}

	public void save(Player player) {
		players.save(player);
	}

	public void removeAllData() {
		players.remove();
	}

	public Player getPlayerByLocationIP(String ip) {
		return players.findOne("{locationIps:#}",ip).as(Player.class);
	}

	public Player getUserById(ObjectId playerId) {
		return players.findOne("{_id:#}",playerId).as(Player.class);
	}
	
	public List<ResearchEntry> getResearchesForRestart() {
		List<ResearchEntry> entries = new ArrayList<ResearchEntry>();

		ResultsIterator<ResearchEntry> agg = players
				.aggregate("{$match : {\"researches.currentlyTraining.id\": {$exists:true} } }")
				.and("{$unwind : \"$researches\" }")
				.and("{$match : {\"researches.currentlyTraining.id\": {$exists:true} } }")
				.and("{$unwind : \"$researches.currentlyTraining\" }")
				.and("{$project : {_id:0,\"trainingResearch\":\"$researches.currentlyTraining\",\"researchMessage.playerEmail\":\"$email\",\"researchMessage.type\":\"$researches.type\",\"researchMessage.id\":\"$researches.currentlyTraining.id\"}}")
				.as(ResearchEntry.class);

		for (ResearchEntry entry : agg) {
			entries.add(entry);
		}

		return entries;
	}

	public void incrementMoneyByPassiveMoneyLevel(int level, long amount) {
//		db.players.update(
//			{"researches": {$elemMatch: {"type":"MONEY_PASSIVE", "level":1}} },
//			{$inc:{money: 10000}},
//			{multi:true}
//		);
		
		WriteResult result = players.update("{\"isNpc\":false,\"researches\": {$elemMatch: {\"type\":\"MONEY_PASSIVE\", \"level\":#}} }",level).multi().with("{$inc:{money: #}}",amount);
		System.out.println("Passive Money Level "+ level + " (£" + amount + ") - Updated " + result.getN() + " players");
	}

	public List<MissionEntry> getMissionsForRestart() {
		List<MissionEntry> entries = new ArrayList<MissionEntry>();

		ResultsIterator<MissionEntry> agg = players
				.aggregate("{$match : {\"missions.type\": {$exists:true} } }")
				.and("{$unwind : \"$missions\" }")
				.and("{$match : {\"missions.type\": {$exists:true} } }")
				.and("{$project : {_id:0,\"playerId\":\"$_id\",\"type\":\"$missions.type\",\"completionTime\":\"$missions.endTime\"}}")
				.as(MissionEntry.class);

		for (MissionEntry entry : agg) {
			entries.add(entry);
		}

		return entries;
	}

	public void updatePlayerToSyndicate(ObjectId playerId, String syndicateId,
			String syndicateName) {
		players.update("{_id:#}",playerId).multi().with("{$set:{syndicateId:#,syndicateName:#}}", syndicateId, syndicateName);
	}

	public void removePlayerFromSyndicate(ObjectId playerId) {
		players.update("{_id:#}",playerId).multi().with("{$unset:{syndicateId:\"\",syndicateName:\"\"}}");
	}

	public List<PlayerDTO> getPlayerDTOSById(List<ObjectId> playerIds) {
		if(playerIds.isEmpty()) {
			return new ArrayList<PlayerDTO>();
		}
		List<String> findList = new ArrayList<String>();
		for (ObjectId playerId : playerIds) {
			findList.add("{_id: {$oid:\""+playerId.toString()+"\"}}");
		}
		
		String query = "{$or: ["+String.join(",", findList)+"]}";
		MongoCursor<PlayerDTO> playerRes = players.find(query).as(PlayerDTO.class);
		
		List<PlayerDTO> playerList = new ArrayList<PlayerDTO>();
		for (PlayerDTO playerDTO : playerRes) {
			playerList.add(playerDTO);
		}
		return playerList;
	}

	
}
