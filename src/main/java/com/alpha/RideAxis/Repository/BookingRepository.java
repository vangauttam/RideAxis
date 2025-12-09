
package com.alpha.RideAxis.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alpha.RideAxis.Entites.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    // add custom queries if needed
}
