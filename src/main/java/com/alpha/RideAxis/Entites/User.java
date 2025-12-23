package com.alpha.RideAxis.Entites;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private long mobileno;
	private String password;
	private String role;


	public User() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public User(int id, long mobileno, String password, String role) {
		super();
		this.id = id;
		this.mobileno = mobileno;
		this.password = password;
		this.role = role;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", mobileno=" + mobileno + ", password=" + password + ", role=" + role + "]";
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public long getMobileno() {
		return mobileno;
	}
	public void setMobileno(long mobileno) {
		this.mobileno = mobileno;
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
	
}
