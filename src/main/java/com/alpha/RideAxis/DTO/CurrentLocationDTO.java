package com.alpha.RideAxis.DTO;

public class CurrentLocationDTO {
	private double latitude;
	private double longitude;
	private String currentcity;
	
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	public String getCurrentcity() {
		return currentcity;
	}
	public void setCurrentcity(String currentcity) {
		this.currentcity = currentcity;
	}
	public CurrentLocationDTO(double latitude, double longitude, String currentcity) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
		this.currentcity = currentcity;
	}
	public CurrentLocationDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "CurrentLocationDTO [latitude=" + latitude + ", longitude=" + longitude + ", currentcity=" + currentcity
				+ "]";
	}
	
	
}
