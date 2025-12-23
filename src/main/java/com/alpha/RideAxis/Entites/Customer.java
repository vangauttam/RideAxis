package com.alpha.RideAxis.Entites;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
@Entity
public class Customer {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String name;
	private int age;
	private String gender;
	private long mobileno;
	private String emailid;
	private String currentloc;
	private boolean activebookingflag;
	private double penaltyamount;
	@OneToMany
	@JsonBackReference("booking-customer")
	private List<Booking> bookinglist;
	@OneToOne
	private User user;

	
	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}


	public Customer() {
		super();
		// TODO Auto-generated constructor stub
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public int getAge() {
		return age;
	}


	public void setAge(int age) {
		this.age = age;
	}


	public String getGender() {
		return gender;
	}


	public void setGender(String gender) {
		this.gender = gender;
	}


	public long getMobileno() {
		return mobileno;
	}


	public void setMobileno(long mobileno) {
		this.mobileno = mobileno;
	}


	public String getEmailid() {
		return emailid;
	}


	public void setEmailid(String emailid) {
		this.emailid = emailid;
	}


	public String getCurrentloc() {
		return currentloc;
	}


	public void setCurrentloc(String currentloc) {
		this.currentloc = currentloc;
	}


	public boolean isActivebookingflag() {
		return activebookingflag;
	}


	public void setActivebookingflag(boolean activebookingflag) {
		this.activebookingflag = activebookingflag;
	}
	public double getPenaltyamount() {
		return penaltyamount;
	}


	public void setPenaltyamount(double penaltyamount) {
		this.penaltyamount = penaltyamount;
	}


	public List<Booking> getBookinglist() {
		return bookinglist;
	}


	public void setBookinglist(List<Booking> bookinglist) {
		this.bookinglist = bookinglist;
	}

	public Customer(int id, String name, int age, String gender, long mobileno, String emailid, String currentloc,
			boolean activebookingflag, double penaltyamount, List<Booking> bookinglist, User user) {
		super();
		this.id = id;
		this.name = name;
		this.age = age;
		this.gender = gender;
		this.mobileno = mobileno;
		this.emailid = emailid;
		this.currentloc = currentloc;
		this.activebookingflag = activebookingflag;
		this.penaltyamount = penaltyamount;
		this.bookinglist = bookinglist;
		this.user = user;
	}


@Override
	public String toString() {
		return "Customer [id=" + id + ", name=" + name + ", age=" + age + ", gender=" + gender + ", mobileno="
				+ mobileno + ", emailid=" + emailid + ", currentloc=" + currentloc + ", activebookingflag="
				+ activebookingflag + ", penaltyamount=" + penaltyamount + ", bookinglist=" + bookinglist + ", user="
				+ user + "]";
	}


public int getId() {
		// TODO Auto-generated method stub
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	

}
