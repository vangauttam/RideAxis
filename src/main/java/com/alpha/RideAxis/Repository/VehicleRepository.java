package com.alpha.RideAxis.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alpha.RideAxis.Entites.Vehicle;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

}

