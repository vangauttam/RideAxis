package com.alpha.RideAxis.DTO;

import com.alpha.RideAxis.Entites.Booking;
import com.alpha.RideAxis.Entites.Customer;
import com.alpha.RideAxis.Entites.Payment;
import com.alpha.RideAxis.Entites.Vehicle;

public class RideCompletionDTO {
	
	private Booking booking; 
	public RideCompletionDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public RideCompletionDTO(Booking booking, Customer customer, Vehicle vehicle, Payment payment) {
		super();
		this.booking = booking;
		this.customer = customer;
		this.vehicle = vehicle;
		this.payment = payment;
	}
	@Override
	public String toString() {
		return "RideCompletionDTO [booking=" + booking + ", customer=" + customer + ", vehicle=" + vehicle
				+ ", payment=" + payment + "]";
	}
	public Booking getBooking() {
		return booking;
	}
	public void setBooking(Booking booking) {
		this.booking = booking;
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
	public Payment getPayment() {
		return payment;
	}
	public void setPayment(Payment payment) {
		this.payment = payment;
	}
	private Customer customer;
	private Vehicle vehicle;
	private Payment payment;
	
	

}
