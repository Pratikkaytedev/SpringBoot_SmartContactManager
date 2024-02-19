package com.smart.service;

import java.util.List;

import com.smart.entity.Contact;
import com.smart.entity.User;

public interface UserService {

	User saveUser(User user);

	int signin(String userName, String password);

	User getUserByUserName(String userName);

	User getUserById(int uId);

	int deleteUserById(int uId);

	int updatePassword(String newPassword,int uId);

	
	

}
