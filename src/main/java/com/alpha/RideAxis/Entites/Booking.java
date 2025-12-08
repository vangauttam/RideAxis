package com.alpha.RideAxis.Entites;

import java.time.LocalDate;

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
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	@ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    private Driver driver;
	private String sourcelocation;
	private String destinationlocation;
	private double distancetravlled;
	private double fare;
	private double estimatedtimerequired;
	private LocalDate bookingdate;
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "payment_id")
	private Payment payement;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public Driver getDriver() {
		return driver;
	}
	public void setDriver(Driver driver) {
		this.driver = driver;
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
	public Payment getPayement() {
		return payement;
	}
	public void setPayement(Payment payement) {
		this.payement = payement;
	}
	@Override
	public String toString() {
		return "Booking [customer=" + customer + ", driver=" + driver + ", sourcelocation=" + sourcelocation
				+ ", destinationlocation=" + destinationlocation + ", distancetravlled=" + distancetravlled + ", fare="
				+ fare + ", estimatedtimerequired=" + estimatedtimerequired + ", bookingdate=" + bookingdate
				+ ", payement=" + payement + "]";
	}
	public Booking(Customer customer, Driver driver, String sourcelocation, String destinationlocation,
			double distancetravlled, double fare, double estimatedtimerequired, LocalDate bookingdate,
			Payment payement) {
		super();
		this.customer = customer;
		this.driver = driver;
		this.sourcelocation = sourcelocation;
		this.destinationlocation = destinationlocation;
		this.distancetravlled = distancetravlled;
		this.fare = fare;
		this.estimatedtimerequired = estimatedtimerequired;
		this.bookingdate = bookingdate;
		this.payement = payement;
	}
	public Booking() {
		super();
		// TODO Auto-generated constructor stub
	}

}
