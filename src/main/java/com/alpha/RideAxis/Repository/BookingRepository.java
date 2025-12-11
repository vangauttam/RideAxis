
package com.alpha.RideAxis.Repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;

import com.alpha.RideAxis.Entites.Booking;
import com.alpha.RideAxis.Entites.Customer;

public interface BookingRepository extends JpaRepository<Booking, Long> {
	List<Booking> findByCustomer(Customer customer);
}
