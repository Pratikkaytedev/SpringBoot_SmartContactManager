package com.smart.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.smart.entity.Contact;
import com.smart.entity.User;
import com.smart.service.ContactService;
import com.smart.service.UserService;

import jakarta.servlet.http.HttpSession;

@RestController
public class SearchController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ContactService contactService;
	
	@GetMapping("/search/{query}")
	public ResponseEntity<?> search(@PathVariable String query,HttpSession session){
		
		System.out.println("query is "+ query);
		
		
		
		String userName = (String)session.getAttribute("userName");
		User user = userService.getUserByUserName(userName);
		
		System.out.println("user is "+ user);
		
	List<Contact> contacts =	contactService.search(query,user);
	
	System.out.println("contacts "+ contacts);
	
	return ResponseEntity.ok(contacts);
	}

}
