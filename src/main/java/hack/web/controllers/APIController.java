package hack.web.controllers;

import java.util.ArrayList;
import java.util.List;

import hack.core.dto.APIResultDTO;
import hack.core.dto.APIResultType;
import hack.core.dto.LocationMapDTO;
import hack.core.models.MissionType;
import hack.core.models.Player;
import hack.core.models.ResearchType;
import hack.core.models.Troop;
import hack.core.models.TroopType;
import hack.core.services.AttackService;
import hack.core.services.LocationService;
import hack.core.services.MissionService;
import hack.core.services.PlayerService;
import hack.core.services.RecruitmentService;
import hack.core.services.ResearchService;
import hack.core.services.SyndicateService;

import org.bson.types.ObjectId;
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
	@Autowired
	private RecruitmentService recruitmentService;
	@Autowired
	private MissionService missionService;
	@Autowired
	private SyndicateService syndicateService;
	
	@PostMapping("/api/research.start")
	public ResponseEntity<APIResultDTO> researchStart(@RequestParam(value = "type") ResearchType type) {
		
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
	public ResponseEntity<APIResultDTO> takeOver(@RequestParam(value = "sourceIp") String sourceIp, @RequestParam(value = "targetIp") String targetIp, @RequestParam(value = "troops") String troopString, @RequestParam(value = "ceo") int ceo) {
		
		List<Troop> desiredTroops = decodeTroops(troopString);
		Player player = playerService.getCurrentPlayer();
		
		APIResultDTO result = attackService.takeoverAttack(player, sourceIp, targetIp, desiredTroops, ceo);
		
	    return new ResponseEntity<APIResultDTO>(result, HttpStatus.OK);
	}
	@PostMapping("/api/attack.defense")
	public ResponseEntity<APIResultDTO> transit(@RequestParam(value = "sourceIp") String sourceIp, @RequestParam(value = "targetIp") String targetIp, @RequestParam(value = "troops") String troopString) {
		
		List<Troop> desiredTroops = decodeTroops(troopString);
		Player player = playerService.getCurrentPlayer();
		
		APIResultDTO result = attackService.sendDefense(player, sourceIp, targetIp, desiredTroops);
		
	    return new ResponseEntity<APIResultDTO>(result, HttpStatus.OK);
	}
	@PostMapping("/api/attack.defense-recall")
	public ResponseEntity<APIResultDTO> recallDefense(@RequestParam(value = "sourceIp") String sourceIp, @RequestParam(value = "targetIp") String targetIp, @RequestParam(value = "types") List<TroopType> types) {
		
		Player player = playerService.getCurrentPlayer();
		
		APIResultDTO result = attackService.triggerReturnDefenseTroops(player, sourceIp, targetIp, types);
		
	    return new ResponseEntity<APIResultDTO>(result, HttpStatus.OK);
	}

	

	@PostMapping("/api/map.location")
	public ResponseEntity<APIResultDTO> mapLocation(@RequestParam(value = "ip") String ip) {
		
		Player player = playerService.getCurrentPlayer();
		
		LocationMapDTO map = locationService.getMapByCentrepoint(player, ip);
		
		APIResultDTO result = new APIResultDTO(APIResultType.SUCCESS, map);
		
	    return new ResponseEntity<APIResultDTO>(result, HttpStatus.OK);
	}
	
	@PostMapping("/api/recruitment.start")
	public ResponseEntity<APIResultDTO> recruitmentStart(@RequestParam(value = "ip") String ip, @RequestParam(value = "type") TroopType type, @RequestParam(value = "no") long no) {
		
		Player player = playerService.getCurrentPlayer();
		APIResultDTO result = recruitmentService.recruit(player, ip, type, no);
		
	    return new ResponseEntity<APIResultDTO>(result, HttpStatus.OK);
	}

	@PostMapping("/api/mission.start")
	public ResponseEntity<APIResultDTO> missionStart(@RequestParam(value = "type") MissionType type) {
		
		Player player = playerService.getCurrentPlayer();
		APIResultDTO result = missionService.triggerMission(player, type);
		
	    return new ResponseEntity<APIResultDTO>(result, HttpStatus.OK);
	}
	

	@PostMapping("/api/syndicate.create")
	public ResponseEntity<APIResultDTO> syndicateCreate(@RequestParam(value = "name") String name) {
		
		Player player = playerService.getCurrentPlayer();
		APIResultDTO result = syndicateService.createSyndicate(player, name);
		
	    return new ResponseEntity<APIResultDTO>(result, HttpStatus.OK);
	}
	
	@PostMapping("/api/syndicate.leave")
	public ResponseEntity<APIResultDTO> syndicateLeave(@RequestParam(value = "playerId", required = false) String playerId) {
		Player player = playerService.getCurrentPlayer();
		if(playerId == null) {
			playerId = player.getId().toString();
		}
		APIResultDTO result = syndicateService.leaveSyndicate(player, playerId);
		
	    return new ResponseEntity<APIResultDTO>(result, HttpStatus.OK);
	}
	@PostMapping("/api/syndicate.join-request")
	public ResponseEntity<APIResultDTO> syndicateJoinRequest(@RequestParam(value = "id") String id) {
		
		Player player = playerService.getCurrentPlayer();
		APIResultDTO result = syndicateService.syndicateJoinRequest(player, id);
		
	    return new ResponseEntity<APIResultDTO>(result, HttpStatus.OK);
	}
	@PostMapping("/api/syndicate.join-approval")
	public ResponseEntity<APIResultDTO> syndicateJoinApproval(@RequestParam(value = "playerId") String playerId, @RequestParam(value = "approve") boolean approve) {
		
		Player player = playerService.getCurrentPlayer();
		APIResultDTO result = syndicateService.syndicateJoinApproval(player, playerId, approve);
		
	    return new ResponseEntity<APIResultDTO>(result, HttpStatus.OK);
	}
	@PostMapping("/api/syndicate.set-admin")
	public ResponseEntity<APIResultDTO> syndicateSetAdmin(@RequestParam(value = "playerId") String playerId, @RequestParam(value = "admin") boolean admin) {
		
		Player player = playerService.getCurrentPlayer();
		APIResultDTO result = syndicateService.syndicateSetAdmin(player, playerId, admin);
		
	    return new ResponseEntity<APIResultDTO>(result, HttpStatus.OK);
	}
	@PostMapping("/api/syndicate.disband")
	public ResponseEntity<APIResultDTO> syndicateDisband(@RequestParam(value = "disband") String disband) {
		
		Player player = playerService.getCurrentPlayer();
		APIResultDTO result = syndicateService.syndicateDisband(player, disband);
		
	    return new ResponseEntity<APIResultDTO>(result, HttpStatus.OK);
	}
	@PostMapping("/api/syndicate.edit-description")
	public ResponseEntity<APIResultDTO> syndicateEditDescription(@RequestParam(value = "description") String description) {
		
		Player player = playerService.getCurrentPlayer();
		APIResultDTO result = syndicateService.syndicateEditDescription(player, description);
		
	    return new ResponseEntity<APIResultDTO>(result, HttpStatus.OK);
	}
	@PostMapping("/api/topic.add-post")
	public ResponseEntity<APIResultDTO> topicAddPost(@RequestParam(value = "content") String content, @RequestParam(value = "topicId") ObjectId topicId) {
		
		Player player = playerService.getCurrentPlayer();
		APIResultDTO result = syndicateService.topicAddPost(player, content, topicId);
		
	    return new ResponseEntity<APIResultDTO>(result, HttpStatus.OK);
	}
	@PostMapping("/api/topic.add-topic")
	public ResponseEntity<APIResultDTO> topicAddTopic(@RequestParam(value = "title") String title) {
		
		Player player = playerService.getCurrentPlayer();
		APIResultDTO result = syndicateService.topicAddTopic(player, title);
		
	    return new ResponseEntity<APIResultDTO>(result, HttpStatus.OK);
	}
	@PostMapping("/api/topic.remove-topic")
	public ResponseEntity<APIResultDTO> topicRemoveTopic(@RequestParam(value = "id") ObjectId topicId) {
		
		Player player = playerService.getCurrentPlayer();
		APIResultDTO result = syndicateService.topicRemoveTopic(player, topicId);
		
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
