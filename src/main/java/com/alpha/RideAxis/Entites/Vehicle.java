package com.alpha.RideAxis.Entites;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
@Entity
public class Vehicle {
	@Id
	private int id;
	private String name;
	private String vehicleno;
	private String type;
	private String model;
	private int capacity;
	private String currentcity;
	private String availableStatus;
	private double priceperkm;
	@OneToOne
	@MapsId
	@JoinColumn(name="id")
	private Driver driver;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	@Override
	public String toString() {
		return "Vehicle [id=" + id + ", name=" + name + ", vehicleno=" + vehicleno + ", type=" + type + ", model="
				+ model + ", capacity=" + capacity + ", currentcity=" + currentcity + ", availableStatus="
				+ availableStatus + ", priceperkm=" + priceperkm + ", driver=" + driver + "]";
	}
	public Vehicle(int id, String name, String vehicleno, String type, String model, int capacity, String currentcity,
			String availableStatus, double priceperkm, Driver driver) {
		super();
		this.id = id;
		this.name = name;
		this.vehicleno = vehicleno;
		this.type = type;
		this.model = model;
		this.capacity = capacity;
		this.currentcity = currentcity;
		this.availableStatus = availableStatus;
		this.priceperkm = priceperkm;
		this.driver = driver;
	}
	public Vehicle() {
		super();
		// TODO Auto-generated constructor stub
	}

}
