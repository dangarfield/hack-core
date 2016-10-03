package hack.web.controllers;

import hack.core.actor.messages.ResearchMessage;
import hack.core.config.security.RegistrationForm;
import hack.core.dao.LocationDAO;
import hack.core.dao.PlayerDAO;
import hack.core.dto.APIResultDTO;
import hack.core.dto.APIResultType;
import hack.core.models.Player;
import hack.core.models.Research;
import hack.core.services.LocationService;
import hack.core.services.PlayerService;
import hack.core.services.ResearchService;

import java.security.Principal;
import java.util.Random;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminController {

	@Autowired
	private PlayerService playerService;
	@Autowired
	private ResearchService researchService;
	@Autowired
	private LocationService locationService;
	@Autowired
	private LocationDAO locationDAO;
	@Autowired
	private PlayerDAO playerDAO;

	@RequestMapping("/admin")
	public String appHome(Model model, Principal principle) {

		model.addAttribute("pageTitle", "Admin");

		// TODO - Add some stats, total players, locations, current upgrades etc
		return "admin";
	}

	@PostMapping("/admin/api/data.remove-all")
	public ResponseEntity<APIResultDTO> removeAllData() {
		locationDAO.removeAllData();
		playerDAO.removeAllData();

		APIResultDTO result = new APIResultDTO(APIResultType.SUCCESS, "Removed all data");

		return new ResponseEntity<APIResultDTO>(result, HttpStatus.OK);
	}

	@PostMapping("/admin/api/player.create-test-users")
	public ResponseEntity<APIResultDTO> createTestUsers(@RequestParam(value = "no") int no) {

		for (int i = 1; i <= no; i++) {
			System.out.println(i);
			RegistrationForm registrationForm = new RegistrationForm();
			int random = 10000 + new Random().nextInt(79999);
			registrationForm.setName("Test " + i + " " + random);
			registrationForm.setEmail("test" + i + "@" + random + ".com");
			registrationForm.setPassword("pass");
			registrationForm.setPassword("pass");
			playerService.createNewPlayer(registrationForm);
		}

		APIResultDTO result = new APIResultDTO(APIResultType.SUCCESS, "Created " + no + " test users");

		return new ResponseEntity<APIResultDTO>(result, HttpStatus.OK);
	}

	@PostMapping("/admin/api/player.create-dg-user")
	public ResponseEntity<APIResultDTO> createDGUser() {

		RegistrationForm registrationForm = new RegistrationForm();
		registrationForm.setName("Dan");
		registrationForm.setEmail("d@g.com");
		registrationForm.setPassword("pass");
		registrationForm.setPassword("pass");
		Player player = playerService.createNewPlayer(registrationForm);
		for (Research research : player.getResearches()) {
			for (int i = 1; i < 5; i++) {
				ResearchMessage researchMessage = new ResearchMessage(player.getEmail(), research.getType(), new ObjectId().toString());
				researchService.completeTraining(researchMessage);
			}
		}
		
		
		APIResultDTO result = new APIResultDTO(APIResultType.SUCCESS, "Created d@g.com user");

		return new ResponseEntity<APIResultDTO>(result, HttpStatus.OK);
	}

}
