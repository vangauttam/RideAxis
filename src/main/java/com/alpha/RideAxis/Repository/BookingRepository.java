
package com.alpha.RideAxis.Repository;

import java.sql.Driver;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alpha.RideAxis.Entites.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> {
	

	
}
