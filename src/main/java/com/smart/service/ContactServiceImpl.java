package com.smart.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.smart.entity.Contact;
import com.smart.entity.User;
import com.smart.repository.ContactRepository;

@Service
public class ContactServiceImpl implements ContactService {

	@Autowired
private	ContactRepository contactRepository;
	
	@Override
	public List<Contact> getAllContacts() {
		
		List<Contact> contacts = contactRepository.findAll();
		
		return contacts;
	}

	@Override
	public Page<Contact> findContactsByUserId(int getuId,Pageable pageable) {
		
		return contactRepository.findContactsByUserId(getuId,pageable);
	}

	@Override
	public Contact findById(int cId) {
		Contact contact = contactRepository.findById(cId).get();
		
		return contact;
	}

	@Override
	public int deleteContactsById(int cId) {
		 contactRepository.deleteById(cId);
		 
		return 1;
	}

	@Override
	public int save(Contact contact) {
		
		Contact save = contactRepository.save(contact);
		
		int i=0;
		
		if(save!=null) {
			i=1;
		}
		
		return i;
	}

	@Override
	public List<Contact> search(String query, User user) {
		
		return contactRepository.findByNameContainingAndUser(query, user);
	}

	
}
