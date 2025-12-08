package com.alpha.RideAxis.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.alpha.RideAxis.Entites.Vehicle;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
	@Query("SELECT v FROM Vehicle v WHERE v.availableStatus = 'Available' AND v.currentcity = :city")
	List<Vehicle> findAvailableVehiclesByCity(String city);
}

