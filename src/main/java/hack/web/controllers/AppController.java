package hack.web.controllers;

import hack.core.dto.LocationStealMoneyDTO;
import hack.core.models.Location;
import hack.core.models.MissionType;
import hack.core.models.Player;
import hack.core.models.Syndicate;
import hack.core.models.TroopType;
import hack.core.services.LocationService;
import hack.core.services.PlayerService;
import hack.core.services.SyndicateService;

import java.security.Principal;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.gson.Gson;

@Controller
public class AppController {

	@Autowired
	private PlayerService playerService;
	@Autowired
	private LocationService locationService;
	@Autowired
	private SyndicateService syndicateService;

	@RequestMapping("/")
	public String siteRoot() {
		return "redirect:/app";
	}
	
	@RequestMapping("/app")
	public String appHome(Model model, Principal principle) {

		Player player = playerService.getCurrentPlayer();
		List<Location> locations = locationService.getLocationsForPlayer(player);
		model.addAttribute("player", player);
		model.addAttribute("locations", locations);
		model.addAttribute("troopTypes", TroopType.values());
		model.addAttribute("missionTypes", MissionType.values());
		model.addAttribute("pageTitle", "App");
		Date now = new Date(new Date().getTime()+(1000*60*60)); //TODO - To cope with BST
		model.addAttribute("now",now);
		return "app";
	}

	@RequestMapping("/app/steal-money")
	public String appMakeMoney(Model model, Principal principle) {

		Player player = playerService.getCurrentPlayer();
		List<Location> locations = locationService.getLocationsForPlayer(player);
		List<LocationStealMoneyDTO> targetLocations = locationService.getLocationListForMakeMoneySearch(player);
		model.addAttribute("player", player);
		model.addAttribute("locations", locations);
		model.addAttribute("targetLocations", targetLocations);
		model.addAttribute("pageTitle", "Steal Money");

		return "steal-money";
	}

	@RequestMapping("/app/map")
	public String appMap(Model model, Principal principle, @RequestParam(value = "ip", required = false) String ip) {

		Player player = playerService.getCurrentPlayer();
		List<Location> locations = locationService.getLocationsForPlayer(player);

		Location location = null;
		String prev = null;
		String next = null;
		
		int locSize = locations.size();
		int foundLocPos = 0;
		for (int i = 0; i < locSize; i++) {
			Location loc = locations.get(i);
			if(loc.getIp().equals(ip)) {
				foundLocPos = i;
				location = loc;
			}
		}
		if(foundLocPos == 0) {
			location = locations.get(0);
		} else {
			prev = locations.get(foundLocPos - 1).getIp();
		}
		if(foundLocPos < locSize -1) {
			next = locations.get(foundLocPos + 1).getIp();
		}

		Gson gson = new Gson();
		model.addAttribute("player", player);
		model.addAttribute("locations", locations);
		model.addAttribute("locationsJson", gson.toJson(locations));
		model.addAttribute("location", location);
		model.addAttribute("locationJson", gson.toJson(location));
		model.addAttribute("next",next);
		model.addAttribute("prev",prev);
		model.addAttribute("pageTitle", "Map");

		return "map";
	}
	
	@RequestMapping("/app/syn/{id}")
	public String syndicateHome(Model model, Principal principle, @PathVariable("id") String id) {

		Player player = playerService.getCurrentPlayer();
		
		Syndicate syndicate = syndicateService.getSyndicateById(id);
		
		model.addAttribute("player", player);
		model.addAttribute("syndicate", syndicate);
		boolean isAdmin = syndicateService.isAdminInSyndicate(player, syndicate);
		model.addAttribute("isPlayerInSyndicate", syndicateService.isPlayerInSyndicate(player, syndicate));
		model.addAttribute("isAdminInSyndicate", isAdmin);
		if(isAdmin) {
			model.addAttribute("players",playerService.getPlayerDTOsById(syndicate.getPlayers()));
			model.addAttribute("admins",playerService.getPlayerDTOsById(syndicate.getAdmins()));
			model.addAttribute("applicants",playerService.getPlayerDTOsById(syndicate.getApplicants()));
		}
		return "syndicate";
	}
	@RequestMapping("/app/syn/{syndicateId}/topic/{topicId}")
	public String syndicateTopic(Model model, Principal principle, @PathVariable("syndicateId") String syndicateId, @PathVariable("topicId") ObjectId topicId) {

		Player player = playerService.getCurrentPlayer();
		
		Syndicate syndicate = syndicateService.getSyndicateById(syndicateId);
		
		model.addAttribute("player", player);
		model.addAttribute("syndicate", syndicate);
		boolean isPlayer = syndicateService.isPlayerInSyndicate(player, syndicate);
		boolean isAdmin = syndicateService.isAdminInSyndicate(player, syndicate);
		model.addAttribute("isPlayerInSyndicate", isPlayer);
		model.addAttribute("isAdminInSyndicate", isAdmin);
		if(isPlayer) {
			model.addAttribute("topic",syndicateService.getTopicById(topicId));
		}
		return "topic";
	}
	
	
}
