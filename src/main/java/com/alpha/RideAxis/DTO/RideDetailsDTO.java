package com.alpha.RideAxis.DTO;

public class RideDetailsDTO {
	private String sourceloc;
	private String destinationloc;
	private double distanceTravelled;
	private double fare;
	
	public RideDetailsDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public RideDetailsDTO(String sourceloc, String destinationloc, double distanceTravelled, double fare) {
		super();
		this.sourceloc = sourceloc;
		this.destinationloc = destinationloc;
		this.distanceTravelled = distanceTravelled;
		this.fare = fare;
	}
	@Override
	public String toString() {
		return "RideDetailsDTO [sourceloc=" + sourceloc + ", destinationloc=" + destinationloc + ", distanceTravelled="
				+ distanceTravelled + ", fare=" + fare + "]";
	}
	public String getSourceloc() {
		return sourceloc;
	}
	public void setSourceloc(String sourceloc) {
		this.sourceloc = sourceloc;
	}
	public String getDestinationloc() {
		return destinationloc;
	}
	public void setDestinationloc(String destinationloc) {
		this.destinationloc = destinationloc;
	}
	public double getDistanceTravelled() {
		return distanceTravelled;
	}
	public void setDistanceTravelled(double distanceTravelled) {
		this.distanceTravelled = distanceTravelled;
	}
	public double getFare() {
		return fare;
	}
	public void setFare(double fare) {
		this.fare = fare;
	}

}
