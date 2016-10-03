package hack.core.services;

import java.util.List;
import java.util.Random;

import hack.core.models.Coord;
import hack.core.models.Location;
import hack.core.models.Player;
import hack.core.dao.LocationDAO;
import hack.core.models.Troop;
import hack.core.models.TroopType;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocationService {

	//private static final Logger LOG = Logger.getLogger("LocationService");

	@Autowired
	private LocationDAO locationDAO;
	

	protected Location createAndSaveLocation(ObjectId playerId, Coord coords, boolean isNpc) {
		Location location = new Location();
		location.setIp(generateIpAddress()); // Generate a unique IP address
		location.setPlayer(playerId);
		location.setCoord(coords);
		location.setNpc(isNpc);
		location.setResearchHideLevel(1);
		
		location.getTroopsOwn().add(new Troop(TroopType.FIREWALL, 10));
		location.getTroopsOwn().add(new Troop(TroopType.PHYSICAL, 10));
		location.getTroopsOwn().add(new Troop(TroopType.DATA, 10));

		locationDAO.saveLocation(location);
		return location;
	}

	private String generateIpAddress() {
		Random r = new Random();
		String ip = r.nextInt(256) + "." + r.nextInt(256) + "."
				+ r.nextInt(256) + "." + r.nextInt(256);
		return locationDAO.doesIPAlreadyExist(ip) ? generateIpAddress() : ip;
	}

	protected Coord getNextCoord(Coord current) {
		int x = current.getX();
		int y = current.getY();
		int level = Math.max(Math.abs(x), Math.abs(y));
		Coord delta = new Coord(0, 0);

		if (-x == level)
			delta.setY(1);
		else if (y == level)
			delta.setX(1);
		else if (x == level)
			delta.setY(-1);
		else if (-y == level)
			delta.setX(-1);

		if (x > 0 && (x == y || x == -y)) {
			delta = new Coord(delta.getY(), 0 - delta.getX());
		}

		x += delta.getX();
		y += delta.getY();

		Coord next = new Coord(x, y);
		return next;
	}
	
	public List<Location> getLocationsForPlayer(Player player) {
		return locationDAO.getLocationsForEmail(player);
	}
	
	public List<Location> getLocationListForMakeMoneySearch(Player player) {
		return locationDAO.getRandomLocations(player, 10);
	}
	
	public void incrementAllOfAPlayersLocationsResearchHideLevel(Player player) {
		locationDAO.incrementAllOfAPlayersLocationsResearchHideLevel(player,1);
	}
}
