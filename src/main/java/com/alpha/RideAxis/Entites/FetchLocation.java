package com.alpha.RideAxis.Entites;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
    name = "fetch_location",
    uniqueConstraints = @UniqueConstraint(columnNames = {"lat","lon"})
)
public class FetchLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;  

    private double lat;
    private double lon;
    private String city;

    public FetchLocation() {}

    public FetchLocation(double lat, double lon, String city) {
        this.lat = lat;
        this.lon = lon;
        this.city = city;
    }
    

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public double getLat() {
        return lat;
    }
    public void setLat(double lat) {
        this.lat = lat;
    }
    public double getLon() {
        return lon;
    }
    public void setLon(double lon) {
        this.lon = lon;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "FetchLocation [id=" + id + ", lat=" + lat + ", lon=" + lon + ", city=" + city + "]";
    }
    
}
