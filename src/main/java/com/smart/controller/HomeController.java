package com.smart.controller;

import java.net.http.HttpRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.smart.entity.User;
//import com.smart.helper.Message;
import com.smart.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
//import jakarta.validation.Valid;

@Controller
public class HomeController {

//	@Autowired
//	private BCryptPasswordEncoder passwordEncoder;

	private static final Logger log = LoggerFactory.getLogger(HomeController.class);

	@Autowired
	private UserService userService;

	@GetMapping("/")
	public String home(Model model) {

		model.addAttribute("title", "Home-Smart Contact Manager");
		log.info("home page called");
		return "home";
	}

	@GetMapping("/about")
	public String about(Model model) {

		model.addAttribute("title", "About-Smart Contact Manager");
		log.info("about page called");
		return "about";
	}

	@GetMapping("/register")
	public String register(Model model) {

		model.addAttribute("Register", "About-Smart Contact Manager");
		model.addAttribute("user", new User());
		return "register";
	}

	@PostMapping("/do_register")
	public String registerUser(/* @Valid */ @ModelAttribute User user, BindingResult bindingResult,
			@RequestParam boolean agreement, Model model, HttpSession session) {

//		System.out.println(agreement);
//		System.out.println(user);

		try {

			if (!agreement) {
				System.out.println("You have not agreed the terms and conditions");
				throw new Exception();
			}

			if (bindingResult.hasErrors()) {
				System.out.println("Error " + bindingResult.toString());
				model.addAttribute("user", user);
				return "signup";
			}

			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setImageUrl("default.png");

//			user.setPassword(passwordEncoder.encode(user.getPassword()));

			User result = userService.saveUser(user);

			System.out.println(result);

			model.addAttribute("user", new User());
//		session.setAttribute("message", new Message("Successfully Registered !!","alert-success" ));
			return "register";

		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("user", user);
//			session.setAttribute("message", new Message("Something went wrong!!"+e.getMessage(),"alert-danger" ));
			return "home";
		}
	}

	@GetMapping("/logins")
	public String customLogin(Model model) {

		// model.addAttribute("title","Login Page");

		return "login";
	}

	@PostMapping("/signin")
	public RedirectView signin(HttpServletRequest request,@RequestParam String userName, @RequestParam String password) {

//		System.out.println("user name "+ userName);

		HttpSession session = request.getSession();
		RedirectView rv = new RedirectView();

		int i = userService.signin(userName, password);
		
		
		if (i > 0) {
			
			String url = "user/index";
			session.setAttribute("userName",userName);
			

			rv.setUrl(url);

//System.out.println(user);

			return rv;
		} else {
			String url = "logins";

			rv.setUrl(url);
			return rv;
		}

	}
	
	

	@GetMapping("/logout")
	public String logout(HttpSession session) {
	    // 1. Invalidate Session
	    invalidateSession(session);
	    log.info("Logout page called");

	    // 2. Set attribute for successful logout
	    session.setAttribute("logoutMessage", "Logout successful");

	    return "login";
	}

	private void invalidateSession(HttpSession session) {
	    if (session != null) {
	        session.invalidate();
	        log.info("Session invalidated");
	    }
	}
	
	
	

	}

