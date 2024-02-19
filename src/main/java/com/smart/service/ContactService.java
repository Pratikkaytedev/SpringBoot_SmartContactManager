package com.smart.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.smart.entity.Contact;
import com.smart.entity.User;

public interface ContactService {

	List<Contact> getAllContacts();

	Page<Contact> findContactsByUserId(int getuId,Pageable pageable);

	Contact findById(int cId);

	int deleteContactsById(int cId);

	int save(Contact contact);

	List<Contact> search(String query, User user);

}
