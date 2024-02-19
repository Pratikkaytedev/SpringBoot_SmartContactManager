package com.smart.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smart.entity.Contact;
import com.smart.entity.User;
import com.smart.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public User saveUser(User user) {
		return userRepository.save(user);
	}

	@Override
	public int signin(String userName, String password) {

		return userRepository.signin(userName, password);
	}

	@Override
	public User getUserByUserName(String userName) {

		return userRepository.getUserByUserName(userName);
	}

	@Override
	public User getUserById(int uId) {
		
		return userRepository.getById(uId);
	}

	@Override
	public int deleteUserById(int uId) {
		userRepository.deleteById(uId);
		
		return 1;
	}

	@Transactional
	@Override
	public int updatePassword(String newPassword,int uId) {
		
		return userRepository.updatePassword(newPassword,uId);
	}

	

}
