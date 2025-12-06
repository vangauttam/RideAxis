package com.alpha.RideAxis.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alpha.RideAxis.Entites.Vehicle;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
	 List<Vehicle> findByAvailableStatus(String availableStatus, String string);

}

