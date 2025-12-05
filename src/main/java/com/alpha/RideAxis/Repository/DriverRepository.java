package com.alpha.RideAxis.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alpha.RideAxis.Entites.Driver;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {
	Driver findByMobileno(long mobileno);


}

