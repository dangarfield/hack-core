package hack.web.controllers;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import hack.core.config.security.RegistrationForm;
import hack.core.config.security.RegistrationValidator;
import hack.core.config.security.SecurityService;
import hack.core.services.LocationService;
import hack.core.services.PlayerService;

@Controller
public class PlayerController {

	@Autowired
	private PlayerService playerService;
	@Autowired
	private LocationService locationService;
	@Autowired
	private SecurityService securityService;
	@Autowired
	private RegistrationValidator registrationValidator;
	
	
	@GetMapping("/login")
	public String loginLandingPage(Model model) {
		model.addAttribute("pageTitle", "Login");
		return "login";
	}

	@GetMapping("/logout")
    public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){    
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login?logout";
    }
	
//	@PostMapping("/login")
//	public String loginPostRequest(@RequestParam(value = "name", required = false, defaultValue = "Worlds") String name, Model model) {
//		model.addAttribute("name", name);
//		return "greeting";
//	}
//	
	@GetMapping("/register")
	public String registerGetRequest(Model model) {
		model.addAttribute("registrationForm", new RegistrationForm());
		model.addAttribute("pageTitle", "Register");
		return "register";
	}
	
	@PostMapping("/register")
	public String registerPostRequest(@ModelAttribute("registrationForm") RegistrationForm registrationForm, BindingResult bindingResult, Model model) {
		registrationValidator.validate(registrationForm, bindingResult);
		//bindingResult.getFieldErrors().get(0).getDefaultMessage();
		if(bindingResult.hasErrors()) {
			model.addAttribute("registrationForm", registrationForm);
			model.addAttribute("bindingResult", bindingResult);
			model.addAttribute("pageTitle", "Register");
			return "register";
		}
		
		playerService.createNewPlayer(registrationForm);
		securityService.autologin(registrationForm.getEmail(), registrationForm.getPassword());
		
		model.addAttribute("regForm", registrationForm);
		return "redirect:/app";
	}

}
