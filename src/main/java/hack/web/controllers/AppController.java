package hack.web.controllers;

import hack.core.models.Location;
import hack.core.models.Player;
import hack.core.services.LocationService;
import hack.core.services.PlayerService;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AppController {
	
	@Autowired
	private PlayerService playerService;
	@Autowired
	private LocationService locationService;
	
	
	@RequestMapping("/app")
	public String appHome(Model model, Principal principle) {
		
		Player player = playerService.getCurrentPlayer();
		List<Location> locations = locationService.getLocationsForPlayer(player);
		model.addAttribute("player", player);
		model.addAttribute("locations", locations);
		model.addAttribute("pageTitle", "App");
		
		return "app";
	}
	
	@RequestMapping("/app/steal-money")
	public String appMakeMoney(Model model, Principal principle) {
		
		Player player = playerService.getCurrentPlayer();
		List<Location> locations = locationService.getLocationsForPlayer(player);
		List<Location> targetLocations = locationService.getLocationListForMakeMoneySearch(player);
		model.addAttribute("player", player);
		model.addAttribute("locations", locations);
		model.addAttribute("targetLocations", targetLocations);
		model.addAttribute("pageTitle", "Make Money");
		
		return "steal-money";
	}
}
