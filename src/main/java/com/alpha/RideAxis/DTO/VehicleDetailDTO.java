package com.alpha.RideAxis.DTO;

import com.alpha.RideAxis.Entites.Vehicle;

public class VehicleDetailDTO {
	private Vehicle v;
	private int fare;
	private int estimatedtime;
	public Vehicle getV() {
		return v;
	}
	public void setV(Vehicle v) {
		this.v = v;
	}
	public int getFare() {
		return fare;
	}
	public void setFare(int fare) {
		this.fare = fare;
	}
	public int getEstimatedtime() {
		return estimatedtime;
	}
	public void setEstimatedtime(int estimatedtime) {
		this.estimatedtime = estimatedtime;
	}
	@Override
	public String toString() {
		return "VehicleDetailDTO [v=" + v + ", fare=" + fare + ", estimatedtime=" + estimatedtime + "]";
	}
	public VehicleDetailDTO(Vehicle v, int fare, int estimatedtime) {
		super();
		this.v = v;
		this.fare = fare;
		this.estimatedtime = estimatedtime;
	}
	public VehicleDetailDTO() {
		super();
	}
	
	 

}
