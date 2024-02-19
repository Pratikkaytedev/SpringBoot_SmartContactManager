//package com.smart.config;
//
//import java.nio.file.attribute.UserPrincipalNotFoundException;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//
//import com.smart.entity.User;
//import com.smart.repository.UserRepository;
//
//public class UserDetailServiceImpl implements UserDetailsService {
//	
//	@Autowired
//	private UserRepository repository;
//
//	@Override
//	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//		
//		 User user = repository.getUserByUserName(username);
//		 
//		 
//		 if(user==null) {
//			 throw new UsernameNotFoundException("Can not found User !!");
//		 }
//		 
//		 CustomUserDetails customUserDetails = new CustomUserDetails(user);
//		 
//		return customUserDetails; 
//	}
//
//}
