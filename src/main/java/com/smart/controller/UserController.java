package com.smart.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

//import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import com.smart.entity.Contact;
import com.smart.entity.User;
import com.smart.helper.Message;
import com.smart.repository.UserRepository;
import com.smart.service.ContactService;
import com.smart.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private ContactService contactService;

	@ModelAttribute
	public void addCommonData(Model model, HttpSession session) {

		String userName = (String) session.getAttribute("userName");

		System.out.println("user name is " + userName);
		User user = userService.getUserByUserName(userName);
		System.out.println("user " + user);

		session.setAttribute("user", user);

	}

	@GetMapping("/index")
	public String dashboard(Model model, HttpSession session /* , Principal principal */) {
		/*
		 * String userName = principal.getName(); System.out.println(userName);
		 */

		// System.out.println("userName is "+ userName);

		model.addAttribute("title", "User Dashboard");

		return "normal/user_dashboard";
	}

	@GetMapping("/add-contact")
	public String openAddContactForm(Model model) {

		System.out.println("called");
		model.addAttribute("title", "Add Contact");
		model.addAttribute("contact", new Contact());

		System.out.println("exxecute");
		return "normal/add_contact_form";
	}

	@PostMapping("/process-contact")
	public String processContact(@ModelAttribute Contact contact, HttpSession session,
			@RequestParam("profileImage") MultipartFile file) {

		try {

			// System.out.println("contact is "+ contact);

			String userName = (String) session.getAttribute("userName");

			// System.out.println("user name is "+ userName);

			User user = userService.getUserByUserName(userName);

			// File uploading

			if (file.isEmpty()) {
//				System.out.println("file is empty");
				contact.setImage("default.png");
			} else {

				// file the file to folder ad update the name to contact
				contact.setImage(file.getOriginalFilename());

				File saveFile = new ClassPathResource("static/IMAGE").getFile();

				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());

				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

//			System.out.println("image uploaded successfully");

			}

			contact.setUser(user);
			user.getContacts().add(contact);
			User saveUser = userService.saveUser(user);

//			System.out.println("saved user is "+ saveUser);

//			Success Message

			session.setAttribute("message", new Message("Your contact is addedd !! Add more...", "success"));

		} catch (Exception e) {
			e.printStackTrace();

//			error Message

			session.setAttribute("message", new Message("something went wrong !! Try again", "danger"));

		}

		return "normal/add_contact_form";
	}

	@GetMapping("/show-contact/{pageNo}")
	public String showContacts(@PathVariable Integer pageNo, Model model, HttpSession session) {

		model.addAttribute("title", "Show User Contacts");

		String name = (String) session.getAttribute("userName");

		User user = userService.getUserByUserName(name);

		// Set current page and total pages in the model
		model.addAttribute("currentPage", pageNo);
		int pageSize = 5; // You can adjust this value based on your requirements
		Page<Contact> contacts = contactService.findContactsByUserId(user.getuId(), PageRequest.of(pageNo, pageSize));

		model.addAttribute("contacts", contacts);

		// Add total pages to the model
		model.addAttribute("totalPages", contacts.getTotalPages());

		return "normal/show_contacts";
	}

	// showing particular contact details
	@GetMapping("/contact/{cId}")
	public String showContactsDetails(@PathVariable int cId, Model model, HttpSession session) {

//		System.out.println("c Id is "+ cId);

		String name = (String) session.getAttribute("userName");

		User user = userService.getUserByUserName(name);

		Contact contact = contactService.findById(cId);

		if (user.getuId() == contact.getUser().getuId()) {
			model.addAttribute("contact", contact);
			model.addAttribute("title", contact.getName());
		}

		return "normal/contact_detail";
	}

	// updating the contact
	@GetMapping("/update-contact/{cId}")
	public String updateContactsById(@PathVariable int cId, Model model) {

		Contact contact = contactService.findById(cId);

		model.addAttribute("title", "update contact");
		model.addAttribute("contact", contact);

		return "normal/updateContact";
	}

	@PostMapping("/updated-contact")
	public String updateContact(@ModelAttribute Contact contact, Model model,
			@RequestParam("profileImage") MultipartFile file, HttpSession session) {

		try {

			Contact oldContactDetail = contactService.findById(contact.getcId());

			if (!file.isEmpty()) {

				System.out.println("file is not empty");
//				delete old photo

				File deleteFile = new ClassPathResource("static/IMAGE").getFile();
				File file1 = new File(deleteFile, oldContactDetail.getImage());
				file1.delete();

//				update new photo

//			  file the file to folder ad update the name to contact
				contact.setImage(file.getOriginalFilename());

				File saveFile = new ClassPathResource("static/IMAGE").getFile();
				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

				contact.setImage(file.getOriginalFilename());

//				System.out.println("Original File Name: " + file.getOriginalFilename());
//				System.out.println("Content Type: " + file.getContentType());
//				System.out.println("File Size: " + file.getSize());

			} else {
				contact.setImage(oldContactDetail.getImage());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		String name = (String) session.getAttribute("userName");
		User user = userService.getUserByUserName(name);

		contact.setUser(user);

		int save = contactService.save(contact);

		session.setAttribute("message", new Message("Your contact is Updated...", "success"));

		return "redirect:/user/contact/" + contact.getcId();

	}

	// Deleting the contact
	@GetMapping("/delete-contact/{cId}")
	public String deleteContactsById(@PathVariable int cId, Model model, HttpSession session) {

		RedirectView rv = new RedirectView();

		int i = contactService.deleteContactsById(cId);

		if (i > 0) {
			System.out.println("deleted successfully");
			session.setAttribute("message", new Message("contact deleted successfully...", "success"));
			return "redirect:/user/show-contact/0";

		} else {
			System.out.println("deleted failed");
			session.setAttribute("message", new Message("contact deleted failed...", "failed"));

			return "redirect:/user/show-contact/0";
		}

	}

	
	@GetMapping("/update-user/{uId}")
	public String updateUserById(@PathVariable int uId,Model model) {
		
	User user =	userService.getUserById(uId);
	
	System.out.println("user is "+ user);
	
	model.addAttribute("user",user);
		
		return "normal/updateUser";
	}
	
	@PostMapping("/updated-user")
	public String updateUser(@ModelAttribute User user, Model model,
			@RequestParam("profileImage") MultipartFile file, HttpSession session) {

		try {

			User oldUserDetail = userService.getUserById(user.getuId());

			if (!file.isEmpty()) {

				System.out.println("file is not empty");
//				delete old photo

				File deleteFile = new ClassPathResource("static/IMAGE").getFile();
				File file1 = new File(deleteFile, oldUserDetail.getImageUrl());
				file1.delete();

//				update new photo

//			  file the file to folder ad update the name to contact
				user.setImageUrl(file.getOriginalFilename());

				File saveFile = new ClassPathResource("static/IMAGE").getFile();
				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

				user.setImageUrl(file.getOriginalFilename());

//				System.out.println("Original File Name: " + file.getOriginalFilename());
//				System.out.println("Content Type: " + file.getContentType());
//				System.out.println("File Size: " + file.getSize());

			} else {
				user.setImageUrl(oldUserDetail.getImageUrl());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		

		User save = userService.saveUser(user);
		
		if(save!=null) {
			session.setAttribute("message", new Message("Your user is Updated...", "success"));

		}else {
			session.setAttribute("message", new Message("Your user is not Updated...", "error"));

		}

		
		return "redirect:/user/update-user/"+user.getuId();

	}
	
	// Deleting the contact
		@GetMapping("/delete-user/{uId}")
		public String deleteUserById(@PathVariable int uId, Model model, HttpSession session) {

			

			int i = userService.deleteUserById(uId);

			if (i > 0) {
				System.out.println("deleted successfully");
				session.setAttribute("message", new Message("User deleted successfully...", "success"));
				return "login";

			} else {
				System.out.println("deleted failed");
				session.setAttribute("message", new Message("User deleted failed...", "failed"));

				return "login";
			}

		}


	@GetMapping("/profile")
	public String yourProfile(Model model, HttpSession session) {

		String name = (String) session.getAttribute("userName");
		User user = userService.getUserByUserName(name);

		model.addAttribute("user", user);

		model.addAttribute("title", "Profile Page");

		return "normal/profile";
	}
	
	@GetMapping("/settings")
	public String setting() {
		return "normal/setting";
	}
	
	@PostMapping("/changePassword")
	public String changePassword(@RequestParam String oldPassword,@RequestParam String newPassword,HttpSession session) {
		
		String name = (String) session.getAttribute("userName");
		User currentUser = userService.getUserByUserName(name);
		
		
		
		if(oldPassword.equalsIgnoreCase(currentUser.getPassword())) {
			currentUser.setPassword(newPassword);
			User saveUser = userService.saveUser(currentUser);
			
			if(saveUser!=null) {
				session.setAttribute("message", new Message("your Password is successfully changed", "success"));
				return "redirect:/user/index";
			}
			
		}else {
			session.setAttribute("message", new Message("your Entered Password is WRONG", "danger"));
			
			
		}
		return "redirect:/user/settings";
		
	}

}
