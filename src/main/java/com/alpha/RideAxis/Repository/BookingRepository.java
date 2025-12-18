package com.alpha.RideAxis.Repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.alpha.RideAxis.Entites.Booking;
import com.alpha.RideAxis.Entites.Customer;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    @Query("SELECT b FROM Booking b WHERE b.customer.id = :customerId AND b.bookingstatus = 'BOOKED'")
    Booking findActiveBookingByCustomerId(@Param("customerId") int customerId);
    
    

    List<Booking> findByCustomer(Customer customer);
    
    
   
    @Query("""
    	    SELECT b
    	    FROM Booking b
    	    WHERE b.vehicle.driver.id = :driverId
    	      AND b.bookingdate = :bookingdate
    	""")
    	List<Booking> findByDriverIdAndBookingDate(
    	        @Param("driverId") long driverId,
    	        @Param("bookingdate") LocalDate bookingdate
    	);


    


    
    
    
    
}
