package com.smart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.smart.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	//User saveUser(User user);
	
	@Query("select u from User u where u.email= :email")
	public User getUserByUserName(@Param("email") String email);

	@Query("select count(u) from User u where u.email= :userName and u.password= :password")
	public int signin(String userName, String password);

	@Modifying
	@Query("update User u set u.password= :newPassword where u.uId= :userId")
	public int updatePassword(String newPassword,int userId);

}
