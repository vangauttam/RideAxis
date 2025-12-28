package com.alpha.RideAxis.Entites;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
public class Booking {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@ManyToOne
    @JoinColumn(name = "customer_id")
	@JsonManagedReference("booking-customer")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    @JsonManagedReference("booking-vehicle")
    private Vehicle vehicle;
	private String sourcelocation;
	private String destinationlocation;
	private double distancetravlled;
	private double fare;
	private double estimatedtimerequired;
	private LocalDate bookingdate;
	private String paymentstatus="NOT PAID";
	
	@OneToOne(mappedBy = "booking",cascade = CascadeType.ALL)
	@JsonManagedReference("booking-payment")
	private Payment payment;
	private String bookingstatus="PENDING";
	private Integer otp;
	private String otpStage;
	public Booking() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public Vehicle getVehicle() {
		return vehicle;
	}
	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}
	public String getSourcelocation() {
		return sourcelocation;
	}
	public void setSourcelocation(String sourcelocation) {
		this.sourcelocation = sourcelocation;
	}
	public String getDestinationlocation() {
		return destinationlocation;
	}
	public void setDestinationlocation(String destinationlocation) {
		this.destinationlocation = destinationlocation;
	}
	public double getDistancetravlled() {
		return distancetravlled;
	}
	public void setDistancetravlled(double distancetravlled) {
		this.distancetravlled = distancetravlled;
	}
	public double getFare() {
		return fare;
	}
	public void setFare(double fare) {
		this.fare = fare;
	}
	public double getEstimatedtimerequired() {
		return estimatedtimerequired;
	}
	public void setEstimatedtimerequired(double estimatedtimerequired) {
		this.estimatedtimerequired = estimatedtimerequired;
	}
	public LocalDate getBookingdate() {
		return bookingdate;
	}
	public void setBookingdate(LocalDate bookingdate) {
		this.bookingdate = bookingdate;
	}
	public String getPaymentstatus() {
		return paymentstatus;
	}
	public void setPaymentstatus(String paymentstatus) {
		this.paymentstatus = paymentstatus;
	}
	public Payment getPayment() {
		return payment;
	}
	public void setPayment(Payment payment) {
		this.payment = payment;
	}
	public String getBookingstatus() {
		return bookingstatus;
	}
	public void setBookingstatus(String bookingstatus) {
		this.bookingstatus = bookingstatus;
	}
	public Integer getOtp() {
		return otp;
	}
	public void setOtp(Integer otp) {
		this.otp = otp;
	}
	public String getOtpStage() {
		return otpStage;
	}
	public void setOtpStage(String otpStage) {
		this.otpStage = otpStage;
	}
	@Override
	public String toString() {
		return "Booking [customer=" + customer + ", vehicle=" + vehicle + ", sourcelocation=" + sourcelocation
				+ ", destinationlocation=" + destinationlocation + ", distancetravlled=" + distancetravlled + ", fare="
				+ fare + ", estimatedtimerequired=" + estimatedtimerequired + ", bookingdate=" + bookingdate
				+ ", paymentstatus=" + paymentstatus + ", payment=" + payment + ", bookingstatus=" + bookingstatus
				+ ", otp=" + otp + ", otpStage=" + otpStage + "]";
	}
	public Booking(Customer customer, Vehicle vehicle, String sourcelocation, String destinationlocation,
			double distancetravlled, double fare, double estimatedtimerequired, LocalDate bookingdate,
			String paymentstatus, Payment payment, String bookingstatus, Integer otp, String otpStage) {
		super();
		this.customer = customer;
		this.vehicle = vehicle;
		this.sourcelocation = sourcelocation;
		this.destinationlocation = destinationlocation;
		this.distancetravlled = distancetravlled;
		this.fare = fare;
		this.estimatedtimerequired = estimatedtimerequired;
		this.bookingdate = bookingdate;
		this.paymentstatus = paymentstatus;
		this.payment = payment;
		this.bookingstatus = bookingstatus;
		this.otp = otp;
		this.otpStage = otpStage;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	
}
