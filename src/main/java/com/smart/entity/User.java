package com.smart.entity;

import java.util.*;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
//import jakarta.validation.constraints.Email;
//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.NotNull;
//import jakarta.validation.constraints.Size;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//import lombok.ToString;

@Entity
@Table(name = "USER")
//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
//@ToString
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int uId;
	
	@Column(unique = true)
//	@NotBlank(message ="Name field is required!!")
//	@Size(min=2,max=20,message="min2 and max 20 chracters are allowed !!")
	private String name;
	
//	@Email
	private String email;

	private String password;
	private String role;
	private boolean enabled;
	private String imageUrl;
	@Column(length = 500)
	private String about;
	private String city;

	@OneToMany(cascade = CascadeType.ALL , fetch = FetchType.LAZY,mappedBy = "user")
	private List<Contact> contacts = new ArrayList();

	public int getuId() {
		return uId;
	}

	public void setuId(int uId) {
		this.uId = uId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public List<Contact> getContacts() {
		return contacts;
	}

	public void setContacts(List<Contact> contacts) {
		this.contacts = contacts;
	}

	public User(int uId, String name, String email, String password, String role, boolean enabled, String imageUrl,
			String about, String city, List<Contact> contacts) {
		super();
		this.uId = uId;
		this.name = name;
		this.email = email;
		this.password = password;
		this.role = role;
		this.enabled = enabled;
		this.imageUrl = imageUrl;
		this.about = about;
		this.city = city;
		this.contacts = contacts;
	}

	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
	    return "User [uId=" + uId + ", name=" + name + ", email=" + email + ", password=" + password + ", role=" + role
	            + ", enabled=" + enabled + ", imageUrl=" + imageUrl + ", about=" + about + ", city=" + city
	            + "]";
	}

	public String getContactsInfo() {
	    StringBuilder contactsInfo = new StringBuilder();
	    for (Contact contact : contacts) {
	        contactsInfo.append(contact.getName()).append(", ");
	    }
	    return "Contacts: " + contactsInfo.toString();
	}


}
