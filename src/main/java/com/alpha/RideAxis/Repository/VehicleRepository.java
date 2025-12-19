package com.alpha.RideAxis.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.alpha.RideAxis.Entites.Booking;
import com.alpha.RideAxis.Entites.Driver;
import com.alpha.RideAxis.Entites.Vehicle;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
	@Query("SELECT v FROM Vehicle v WHERE v.availableStatus = 'Available' AND v.currentcity = :city")
	List<Vehicle> findAvailableVehiclesByCity(String city);

	@Query("""
	        SELECT b FROM Booking b
	        WHERE b.vehicle.driver.mobileno = :mobileno
	        AND b.bookingstatus = 'ACTIVE'
	    """)
	    Booking findActiveBookingOfDriver(@Param("mobileno") long mobileno);

	Optional<Driver> findByDriver(Driver driver);

}




