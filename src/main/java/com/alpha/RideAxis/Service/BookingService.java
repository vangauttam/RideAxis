package com.alpha.RideAxis.Service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alpha.RideAxis.DTO.BookingDTO;
import com.alpha.RideAxis.Entites.Booking;
import com.alpha.RideAxis.Entites.Customer;
import com.alpha.RideAxis.Entites.Payment;
import com.alpha.RideAxis.Entites.Vehicle;
import com.alpha.RideAxis.Repository.BookingRepository;
import com.alpha.RideAxis.Repository.CustomerRepository;
import com.alpha.RideAxis.Repository.VehicleRepository;

@Service
public class BookingService {

    @Autowired
    private CustomerRepository customerRepo;

    @Autowired
    private VehicleRepository vehicleRepo;

    @Autowired
    private BookingRepository bookingRepo;

    @Transactional
    public Booking bookVehicle(long mobno, BookingDTO dto) {

        // 1️⃣ FIND CUSTOMER BY MOBILE NUMBER
    	Customer cust = customerRepo.findByMobileno(mobno)
    	        .orElseThrow(() -> new RuntimeException("Customer not found: " + mobno));

        if (cust == null) {
            throw new RuntimeException("Customer not found: " + mobno);
        }

        // 2️⃣ FIND VEHICLE BY ID
        Vehicle veh = vehicleRepo.findById(dto.getVehicleId())
                .orElseThrow(() -> new RuntimeException("Vehicle not found with id: " + dto.getVehicleId()));

        // 3️⃣ CREATE BOOKING OBJECT
        Booking booking = new Booking();
        booking.setCustomer(cust);
        booking.setVehicle(veh);

        booking.setSourcelocation(dto.getSourceLoc());
        booking.setDestinationlocation(dto.getDestinationLoc());
        booking.setDistancetravlled(dto.getDistanceTravelled());
        booking.setEstimatedtimerequired(dto.getEstimatedTime());
        booking.setFare(dto.getFare());

        // booking date → today's date
        booking.setBookingdate(LocalDate.now());

       

        // 4️⃣ CREATE PAYMENT ENTITY
        Payment payment = new Payment();
        payment.setCustomer(cust);
        payment.setVehicle(veh);
        payment.setBooking(booking);
        payment.setAmount(dto.getFare());
        payment.setPaymenttype("not paid"); // initial status

        booking.setPayement(payment);

        // 5️⃣ SAVE BOOKING
        Booking savedBooking = bookingRepo.save(booking);

        // 6️⃣ ADD BOOKING TO CUSTOMER BOOKING LIST
        cust.getBookinglist().add(savedBooking);
        customerRepo.save(cust);

        return savedBooking;
    }
}
