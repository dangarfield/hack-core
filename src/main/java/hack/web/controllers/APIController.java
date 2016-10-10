package hack.web.controllers;

import java.util.ArrayList;
import java.util.List;

import hack.core.dto.APIResultDTO;
import hack.core.dto.APIResultType;
import hack.core.dto.LocationMapDTO;
import hack.core.models.Player;
import hack.core.models.ResearchType;
import hack.core.models.Troop;
import hack.core.models.TroopType;
import hack.core.services.AttackService;
import hack.core.services.LocationService;
import hack.core.services.PlayerService;
import hack.core.services.ResearchService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class APIController {

	@Autowired
	private ResearchService researchService;
	@Autowired
	private PlayerService playerService;
	@Autowired
	private AttackService attackService;
	@Autowired
	private LocationService locationService;
	
	@PostMapping("/api/start.training")
	public ResponseEntity<APIResultDTO> startTraining(@RequestParam(value = "type") ResearchType type) {
		
		Player player = playerService.getCurrentPlayer();
		APIResultDTO result = researchService.triggerUpgrade(player, type);
		
	    return new ResponseEntity<APIResultDTO>(result, HttpStatus.OK);
	}

	@PostMapping("/api/attack.steal-money")
	public ResponseEntity<APIResultDTO> stealMoney(@RequestParam(value = "ip") String ip) {
		
		Player player = playerService.getCurrentPlayer();
		
		APIResultDTO result = attackService.stealMoneyAttack(player, ip);
		
	    return new ResponseEntity<APIResultDTO>(result, HttpStatus.OK);
	}
	@PostMapping("/api/attack.scan")
	public ResponseEntity<APIResultDTO> scan(@RequestParam(value = "ip") String ip) {
		
		Player player = playerService.getCurrentPlayer();
		
		APIResultDTO result = attackService.scan(player, ip);
		
	    return new ResponseEntity<APIResultDTO>(result, HttpStatus.OK);
	}

	@PostMapping("/api/attack.takeover")
	public ResponseEntity<APIResultDTO> takeOver(@RequestParam(value = "sourceIp") String sourceIp, @RequestParam(value = "targetIp") String targetIp, @RequestParam(value = "troops") String troopString) {
		
		List<Troop> desiredTroops = decodeTroops(troopString);
		Player player = playerService.getCurrentPlayer();
		
		APIResultDTO result = attackService.takeoverAttack(player, sourceIp, targetIp, desiredTroops);
		
	    return new ResponseEntity<APIResultDTO>(result, HttpStatus.OK);
	}

	

	@PostMapping("/api/map.location")
	public ResponseEntity<APIResultDTO> mapLocation(@RequestParam(value = "ip") String ip) {
		
		Player player = playerService.getCurrentPlayer();
		
		LocationMapDTO map = locationService.getMapByCentrepoint(player, ip);
		
		APIResultDTO result = new APIResultDTO(APIResultType.SUCCESS, map);
		
	    return new ResponseEntity<APIResultDTO>(result, HttpStatus.OK);
	}
	
	
	private List<Troop> decodeTroops(String troopString) {
		
		List<Troop> troops = new ArrayList<Troop>();
		String[] troopItems = troopString.split("-_-");
		for (String troopItem : troopItems) {
			String[] troopItemSplit = troopItem.split("___");
			TroopType type = TroopType.valueOf(troopItemSplit[0]);
			int noOfTroops = Integer.valueOf(troopItemSplit[1]).intValue();
			Troop troop = new Troop(type, noOfTroops);
			troops.add(troop);
		}
		
		return troops;
	}
	
	
	
}
