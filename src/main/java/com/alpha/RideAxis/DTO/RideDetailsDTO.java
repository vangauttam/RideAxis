package com.alpha.RideAxis.DTO;

public class RideDetailsDTO {
	private String sourceloc;
	private String destinationloc;
	private double distanceTravelled;
	private double fare;

	private String paymentstatus;

	public RideDetailsDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RideDetailsDTO(String sourceloc, String destinationloc, double distanceTravelled, double fare,
			String paymentstatus) {
		super();
		this.sourceloc = sourceloc;
		this.destinationloc = destinationloc;
		this.distanceTravelled = distanceTravelled;
		this.fare = fare;
		this.paymentstatus = paymentstatus;
	}

	@Override
	public String toString() {
		return "RideDetailsDTO [sourceloc=" + sourceloc + ", destinationloc=" + destinationloc + ", distanceTravelled="
				+ distanceTravelled + ", fare=" + fare + ", paymentstatus=" + paymentstatus + "]";
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

	public String getPaymentstatus() {
		return paymentstatus;
	}

	public void setPaymentstatus(String paymentstatus) {
		this.paymentstatus = paymentstatus;
	}
}
