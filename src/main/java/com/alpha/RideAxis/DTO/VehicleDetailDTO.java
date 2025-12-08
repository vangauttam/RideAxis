package com.alpha.RideAxis.DTO;

import com.alpha.RideAxis.Entites.Vehicle;

public class VehicleDetailDTO {
	private Vehicle vehicle;
	private double fare;
	private double estimatedtime;
	public Vehicle getVehicle() {
		return vehicle;
	}
	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}
	public double getFare() {
		return fare;
	}
	public void setFare(double fare) {
		this.fare = fare;
	}
	public double getEstimatedtime() {
		return estimatedtime;
	}
	public void setEstimatedtime(double estimatedtime) {
		this.estimatedtime = estimatedtime;
	}
	@Override
	public String toString() {
		return "VehicleDetailDTO [vehicle=" + vehicle + ", fare=" + fare + ", estimatedtime=" + estimatedtime + "]";
	}
	public VehicleDetailDTO(Vehicle vehicle, double fare, double estimatedtime) {
		super();
		this.vehicle = vehicle;
		this.fare = fare;
		this.estimatedtime = estimatedtime;
	}
	public VehicleDetailDTO() {
		super();
	}

	
	
	 

}