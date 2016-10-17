package hack.core.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import hack.core.models.AttackLog;
import hack.core.models.Coord;
import hack.core.models.Location;
import hack.core.models.Player;
import hack.core.dao.LocationDAO;
import hack.core.dao.MapTileEntry;
import hack.core.dto.LocationMapDTO;
import hack.core.dto.LocationMapItemDTO;
import hack.core.dto.LocationMapItemType;
import hack.core.dto.LocationStealMoneyDTO;
import hack.core.models.Troop;
import hack.core.models.TroopType;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocationService {

	// private static final Logger LOG = Logger.getLogger("LocationService");

	@Autowired
	private LocationDAO locationDAO;

	protected Location createAndSaveLocation(ObjectId playerId, Coord coords, boolean isNpc) {
		Location location = new Location();
		location.setIp(generateIpAddress()); // Generate a unique IP address
		location.setPlayer(playerId);
		location.setCoord(coords);
		location.setNpc(isNpc);
		location.setResearchHideLevel(1);

		location.getDefense().add(new Troop(TroopType.FIREWALL, 10));
		location.getDefense().add(new Troop(TroopType.PHYSICAL, 10));
		location.getDefense().add(new Troop(TroopType.DATA, 10));

		locationDAO.saveLocation(location);
		return location;
	}

	private String generateIpAddress() {
		Random r = new Random();
		String ip = r.nextInt(256) + "." + r.nextInt(256) + "." + r.nextInt(256) + "." + r.nextInt(256);
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

	public List<LocationStealMoneyDTO> getLocationListForMakeMoneySearch(Player player) {
		List<LocationStealMoneyDTO> dtos = new ArrayList<LocationStealMoneyDTO>();
		List<Location> locations = locationDAO.getRandomLocations(player, 10);

		for (Location location : locations) {
			AttackLog recentAttack = player.getRecentAttackForIp(location.getIp());
			if (recentAttack == null) {
				dtos.add(new LocationStealMoneyDTO(location, null));
			} else {
				Date cooldownTime = new Date(recentAttack.getTime().getTime() - new Date().getTime());
				dtos.add(new LocationStealMoneyDTO(location, cooldownTime));
			}

		}
		return dtos;
	}

	public void incrementAllOfAPlayersLocationsResearchHideLevel(Player player, int level) {
		locationDAO.incrementAllOfAPlayersLocationsResearchHideLevel(player, level);
	}

	public List<LocationMapItemDTO> getMapByTiles(Player player) {
		return getMapByTiles(player, player.getLocationIps().get(0));
	}

	public List<LocationMapItemDTO> getMapByTiles(Player player, String centreOnIp) {
		Location location = locationDAO.getLocationFromIp(centreOnIp);
		
		int x = location.getCoord().getX() - (location.getCoord().getX() % 100);
		int y = location.getCoord().getY() - (location.getCoord().getY() % 100);
		
		List<MapTileEntry> mapTile = locationDAO.getMapTile(player, x, y);
		
		List<LocationMapItemDTO> mapDTOs = new ArrayList<LocationMapItemDTO>();
		for (MapTileEntry mapTileEntry : mapTile) {
			
			double distance = calculateDistance(location.getCoord(), mapTileEntry.getCoord());
			LocationMapItemType type = getLocationMapItemType(player, mapTileEntry);
			boolean centrePoint = false;
			if(location.getIp().equals(mapTileEntry.getIp())){
				centrePoint = true;
			}
			mapDTOs.add(new LocationMapItemDTO(mapTileEntry.getIp(), distance, type, mapTileEntry.getCoord().getX(), mapTileEntry.getCoord().getY(), centrePoint));
		}
		
		return mapDTOs;
	}
	
	public LocationMapDTO getMapByCentrepoint(Player player, String centreOnIp) {
		Location location = locationDAO.getLocationFromIp(centreOnIp);
		
		int x = location.getCoord().getX() - 50;
		int y = location.getCoord().getY() - 50;
		
		List<MapTileEntry> mapTile = locationDAO.getMapTile(player, x, y);
		
		List<LocationMapItemDTO> mapDTOs = new ArrayList<LocationMapItemDTO>();
		for (MapTileEntry mapTileEntry : mapTile) {
			
			double distance = calculateDistance(location.getCoord(), mapTileEntry.getCoord());
			LocationMapItemType type = getLocationMapItemType(player, mapTileEntry);
			boolean centrePoint = false;
			if(location.getIp().equals(mapTileEntry.getIp())){
				centrePoint = true;
			}
			mapDTOs.add(new LocationMapItemDTO(mapTileEntry.getIp(), distance, type, mapTileEntry.getCoord().getX(), mapTileEntry.getCoord().getY(), centrePoint));
		}
		
		LocationMapDTO map = new LocationMapDTO(location, mapDTOs);
		return map;
	}

	public long calculateAttackTransitTimeInSeconds(Location source, Location target) {
		double distance = calculateDistance(source.getCoord(), target.getCoord());
		long time = (long) distance * 60 * 30; //1 distance = 30 mins
		time = 30; //TODO - Set to 30 seconds no matter when, change this when in production
		return time;
	}
	private double calculateDistance(Coord p1, Coord p2) {
		double dx = p1.getX() - p2.getX();
        double dy = p1.getY() - p2.getY();
        return Math.sqrt(dx * dx + dy * dy);
	}
	private LocationMapItemType getLocationMapItemType(Player player, MapTileEntry mapTileEntry) {
		if(player.getId().equals(mapTileEntry.getPlayer())) {
			return LocationMapItemType.PLAYER;
		} else if(mapTileEntry.isNpc()) {
			return LocationMapItemType.NPC;
		} else {
			return LocationMapItemType.OTHER_PLAYER;
		}
	}

	public Location getLocationFromIp(String ip) {
		return locationDAO.getLocationFromIp(ip);
	}

	public void save(Location location) {
		locationDAO.saveLocation(location);
	}

	public Location getRandomLocationForPlayer(Player player) {
		return locationDAO.getRandomLocationForPlayer(player);
	}
	
}
