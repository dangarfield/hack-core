package hack.core.dao;

import hack.core.models.Player;

import org.bson.types.ObjectId;
import org.jongo.MongoCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
