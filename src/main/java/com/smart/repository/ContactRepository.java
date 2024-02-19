package com.smart.repository;

import java.util.List;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.smart.entity.Contact;
import com.smart.entity.User;

@Repository
public interface ContactRepository extends JpaRepository<Contact,Integer> {
	
	

	@Query("from Contact as c where c.user.uId=:userId")
	public Page<Contact> findContactsByUserId(@Param("userId")int userId,Pageable pageable);

	
//	@Query("delete from Contact c where c.cId=:cId")
//	public int deleteById(@Param("cId") int cId);

	
//	Search
	public List<Contact> findByNameContainingAndUser(String name,User user);
}
