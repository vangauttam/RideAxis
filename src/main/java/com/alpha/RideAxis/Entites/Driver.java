package com.alpha.RideAxis.Entites;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
@Entity
@Table(name = "drivers")
public class Driver {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long driverid;
	private String licenceno;
	private String upiid;
	private String dname;
	private String status;
	private int age;
	@Column(unique = true, nullable = false)
	private long mobileno;
	private String gender;
	private String mailid;
	@JsonIgnore
	@OneToOne(mappedBy = "driver",cascade = CascadeType.ALL)
	private Vehicle vehicle;
	
	
	@OneToOne(optional = false)
	@JoinColumn(name = "userr_id", nullable = false)
	private Userr userr;
	
	public Userr getUserr() {
		return userr;
	}
	public void setUserr(Userr userr) {
		this.userr = userr;
	}
	@OneToMany
	private List<Booking> bookinglist;
	public List<Booking> getBookinglist() {
		return bookinglist;
	}
	public void setBookinglist(List<Booking> bookinglist) {
		this.bookinglist = bookinglist;
	}
	public Long getDriverid() {
		return driverid;
	}
	public void setDriverid(Long driverid) {
		this.driverid = driverid;
	}
	public String getLicenceno() {
		return licenceno;
	}
	public void setLicenceno(String licenceno) {
		this.licenceno = licenceno;
	}
	public String getUpiid() {
		return upiid;
	}
	public void setUpiid(String upiid) {
		this.upiid = upiid;
	}
	public String getDname() {
		return dname;
	}
	public void setDname(String dname) {
		this.dname = dname;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public long getMobileno() {
		return mobileno;
	}
	public void setMobileno(long mobileno) {
		this.mobileno = mobileno;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getMailid() {
		return mailid;
	}
	public void setMailid(String mailid) {
		this.mailid = mailid;
	}
	public Vehicle getVehicle() {
		return vehicle;
	}
	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}
	
	public Driver() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "Driver [driverid=" + driverid + ", licenceno=" + licenceno + ", upiid=" + upiid + ", dname=" + dname
				+ ", status=" + status + ", age=" + age + ", mobileno=" + mobileno + ", gender=" + gender + ", mailid="
				+ mailid + ", vehicle=" + vehicle + ", userr=" + userr + ", bookinglist=" + bookinglist + "]";
	}
	
	public Driver(Long driverid, String licenceno, String upiid, String dname, String status, int age, long mobileno,
			String gender, String mailid, Vehicle vehicle, Userr userr, List<Booking> bookinglist) {
		super();
		this.driverid = driverid;
		this.licenceno = licenceno;
		this.upiid = upiid;
		this.dname = dname;
		this.status = status;
		this.age = age;
		this.mobileno = mobileno;
		this.gender = gender;
		this.mailid = mailid;
		this.vehicle = vehicle;
		this.userr = userr;
		this.bookinglist = bookinglist;
	}
	

}
