package hack.core.services;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;

import hack.core.config.security.RegistrationForm;
import hack.core.dao.ConfigDAO;
import hack.core.dao.PlayerDAO;
import hack.core.dto.PlayerDTO;
import hack.core.models.Coord;
import hack.core.models.Location;
import hack.core.models.Player;
import hack.core.models.Research;
import hack.core.models.ResearchType;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PlayerService {

	private static final Logger LOG = Logger.getLogger("PlayerService");

	@Autowired
	private LocationService locationService;
	@Autowired
	private ConfigDAO configDAO;
	@Autowired
	private PlayerDAO playerDAO;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	public synchronized Player createNewPlayer(RegistrationForm registrationForm) {

		Coord coord = configDAO.getNewestCoord();

		if (playerDAO.doesPlayerAlreadyExist(registrationForm.getEmail())) {
			return null;
		}
		;

		boolean playerLocationCreated = false;
		Player player = null;
		while (!playerLocationCreated) {
			double prob = Math.random();
			coord = locationService.getNextCoord(coord);
			configDAO.saveNewestCoord(coord);

			if (prob >= 0.9D) {
				LOG.info("PLAYER - " + coord + " - " + prob);
				playerLocationCreated = true;
				player = createPlayer(registrationForm.getName(),
						registrationForm.getEmail(),
						registrationForm.getPassword(), coord, false);
			} else if (prob >= 0.7D) {
				LOG.info("NPC    - " + coord + " - " + prob);
				createPlayer("npc", "npc@hack.com", "", coord, true);
			} else {
				LOG.info("SPACE  - " + coord + " - " + prob);
			}
		}

		return player;
	}

	public Player getPlayerByEmail(String email) {
		return playerDAO.getUserByEmail(email);
	}

	public Player getCurrentPlayer() {
		String email = getEmailOfCurrentPrincipal();
		return playerDAO.getUserByEmail(email);
	}

	private Player createPlayer(String name, String email, String password,
			Coord coord, boolean npc) {
		Player player = new Player();
		player.setId(new ObjectId());
		player.setName(name);
		player.setEmail(email);
		player.setPassword(password);
		player.setNpc(npc);
		player.setResearches(createDefaultResearch());

		Location location = locationService.createAndSaveLocation(
				player.getId(), coord, npc);
		player.getLocationIps().add(location.getIp());

		player.setMoney(10000);
		player.setCeoCount(0);
		playerDAO.save(player);
		return player;
	}

	private Set<Research> createDefaultResearch() {
		Set<Research> researches = new HashSet<Research>();
		for (ResearchType type : ResearchType.values()) {
			researches.add(new Research(type, new Random().nextInt(5 - 1) + 1));
		}
		return researches;
	}

	private String getEmailOfCurrentPrincipal() {
		String userName = null;
		Object principal = SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();

		if (principal instanceof UserDetails) {
			userName = ((UserDetails) principal).getUsername();
		} else {
			userName = principal.toString();
		}
		return userName;
	}

	public Player getPlayerByLocationIP(String ip) {
		return playerDAO.getPlayerByLocationIP(ip);
	}

	public void save(Player player) {
		playerDAO.save(player);
	}

	public Player getPlayerById(ObjectId playerId) {
		return playerDAO.getUserById(playerId);
	}

	public Player getUserByEmail(String email) {
		return playerDAO.getUserByEmail(email);
	}

	public void removePlayerAndLocationsFromSyndicate(ObjectId playerId) {
		playerDAO.removePlayerFromSyndicate(playerId);
		locationService.removeAllPlayersLocationFromSyndicate(playerId);
	}
	public void updatePlayerAndLocationsToSyndicate(ObjectId playerId,
			String syndicateId, String syndicateName) {
		playerDAO.updatePlayerToSyndicate(playerId, syndicateId, syndicateName);
		locationService.updateAllPlayersLocationToSyndicate(playerId, syndicateId, syndicateName);
	}

	public List<PlayerDTO> getPlayerDTOsById(List<ObjectId> playerIds) {
		return playerDAO.getPlayerDTOSById(playerIds);
	}
}
