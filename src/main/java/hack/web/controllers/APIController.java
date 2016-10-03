package hack.web.controllers;

import hack.core.dto.APIResultDTO;
import hack.core.models.Player;
import hack.core.models.ResearchType;
import hack.core.services.AttackService;
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
	
	
	
	
}
