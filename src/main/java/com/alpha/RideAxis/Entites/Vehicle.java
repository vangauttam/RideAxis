package com.alpha.RideAxis.Entites;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
@Entity
@Table(name = "vehicles")
public class Vehicle {
	@Id
	private Long vehicleid;
	private String vname;
	private String vehicleno;
	private String type;
	private String model;
	private int capacity;
	private String currentcity;
	private String availableStatus;
	private double priceperkm;
	private double latitude;
	private double longitude;

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
	@OneToOne
	@MapsId
	@JoinColumn(name="vehicleid")
	@JsonIgnore
	private Driver driver;
	public Long getVehicleid() {
		return vehicleid;
	}
	public void setVehicleid(Long vehicleid) {
		this.vehicleid = vehicleid;
	}
	public String getVname() {
		return vname;
	}
	public void setVname(String vname) {
		this.vname = vname;
	}
	public String getVehicleno() {
		return vehicleno;
	}
	public void setVehicleno(String vehicleno) {
		this.vehicleno = vehicleno;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public int getCapacity() {
		return capacity;
	}
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	public String getCurrentcity() {
		return currentcity;
	}
	public void setCurrentcity(String currentcity) {
		this.currentcity = currentcity;
	}
	public String getAvailableStatus() {
		return availableStatus;
	}
	public void setAvailableStatus(String availableStatus) {
		this.availableStatus = availableStatus;
	}
	public double getPriceperkm() {
		return priceperkm;
	}
	public void setPriceperkm(double priceperkm) {
		this.priceperkm = priceperkm;
	}
	public Driver getDriver() {
		return driver;
	}
	public void setDriver(Driver driver) {
		this.driver = driver;
	}
	
	public Vehicle(Long vehicleid, String vname, String vehicleno, String type, String model, int capacity,
			String currentcity, String availableStatus, double priceperkm, double latitude, double longitude,
			Driver driver) {
		super();
		this.vehicleid = vehicleid;
		this.vname = vname;
		this.vehicleno = vehicleno;
		this.type = type;
		this.model = model;
		this.capacity = capacity;
		this.currentcity = currentcity;
		this.availableStatus = availableStatus;
		this.priceperkm = priceperkm;
		this.latitude = latitude;
		this.longitude = longitude;
		this.driver = driver;
	}
	
	public Vehicle() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "Vehicle [vehicleid=" + vehicleid + ", vname=" + vname + ", vehicleno=" + vehicleno + ", type=" + type
				+ ", model=" + model + ", capacity=" + capacity + ", currentcity=" + currentcity + ", availableStatus="
				+ availableStatus + ", priceperkm=" + priceperkm + ", latitude=" + latitude + ", longitude=" + longitude
				+ ", driver=" + driver + "]";
	}
	
	

}
