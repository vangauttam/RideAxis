package com.alpha.RideAxis.DTO;

import com.alpha.RideAxis.Entites.Customer;
import com.alpha.RideAxis.Entites.Vehicle;

public class BookingDTO {

	private Long vehicleId;
	private Customer customer;
    private Vehicle vehicle;
    private String sourceLoc;
    private String destinationLoc;
    private double fare;
    private double distanceTravelled;
    private double estimatedTime;
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
	public String getSourceLoc() {
		return sourceLoc;
	}
	public void setSourceLoc(String sourceLoc) {
		this.sourceLoc = sourceLoc;
	}
	public String getDestinationLoc() {
		return destinationLoc;
	}
	public void setDestinationLoc(String destinationLoc) {
		this.destinationLoc = destinationLoc;
	}
	public double getFare() {
		return fare;
	}
	public void setFare(double fare) {
		this.fare = fare;
	}
	public double getDistanceTravelled() {
		return distanceTravelled;
	}
	public void setDistanceTravelled(double distanceTravelled) {
		this.distanceTravelled = distanceTravelled;
	}
	public double getEstimatedTime() {
		return estimatedTime;
	}
	public void setEstimatedTime(double estimatedTime) {
		this.estimatedTime = estimatedTime;
	}
	@Override
	public String toString() {
		return "BookingDTO [vehicleId=" + vehicleId + "]";
	}
	public BookingDTO(Customer customer, Vehicle vehicle, String sourceLoc, String destinationLoc, double fare,
			double distanceTravelled, double estimatedTime) {
		super();
		this.customer = customer;
		this.vehicle = vehicle;
		this.sourceLoc = sourceLoc;
		this.destinationLoc = destinationLoc;
		this.fare = fare;
		this.distanceTravelled = distanceTravelled;
		this.estimatedTime = estimatedTime;
	}
	public BookingDTO() {
		super();
	}
	public Long getVehicleId() {
		// TODO Auto-generated method stub
		return null;
	}
	public void setVehicleId(Long vehicleId) {
		this.vehicleId = vehicleId;
	}
	public BookingDTO(Long vehicleId) {
		super();
		this.vehicleId = vehicleId;
	}
    
}
