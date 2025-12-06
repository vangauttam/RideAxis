package com.alpha.RideAxis.Entites;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
	@OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
	private List<Booking> bookinglist;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public List<Booking> getBookinglist() {
		return bookinglist;
	}
	public void setBookinglist(List<Booking> bookinglist) {
		this.bookinglist = bookinglist;
	}
	@Override
	public String toString() {
		return "Customer [name=" + name + ", age=" + age + ", gender=" + gender + ", mobileno=" + mobileno
				+ ", emailid=" + emailid + ", currentloc=" + currentloc + ", bookinglist=" + bookinglist + "]";
	}
	public Customer(String name, int age, String gender, long mobileno, String emailid, String currentloc,
			List<Booking> bookinglist) {
		super();
		this.name = name;
		this.age = age;
		this.gender = gender;
		this.mobileno = mobileno;
		this.emailid = emailid;
		this.currentloc = currentloc;
		this.bookinglist = bookinglist;
	}
	public Customer() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

}
