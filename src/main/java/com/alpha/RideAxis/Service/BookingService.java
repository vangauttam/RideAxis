package com.alpha.RideAxis.Service;

import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alpha.RideAxis.ResponseStructure;
import com.alpha.RideAxis.DTO.BookingDTO;
import com.alpha.RideAxis.Entites.Booking;
import com.alpha.RideAxis.Entites.Customer;
import com.alpha.RideAxis.Entites.Driver;
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
    private BookingRepository bookingrepo;
    private Driver d;
    
 

    @Transactional
    public ResponseStructure<Booking> bookVehicle(long mobno, BookingDTO dto) {

        // 1️⃣ FIND CUSTOMER BY MOBILE NUMBER
        Customer customer = customerRepo.findByMobileno(mobno)
                .orElseThrow(() -> new RuntimeException("Customer not found: " + mobno));

        // 2️⃣ FIND VEHICLE BY ID
        Vehicle veh = vehicleRepo.findById(dto.getVehicleId())
                .orElseThrow(() -> new RuntimeException("Vehicle not found with id: " + dto.getVehicleId()));

        
      
        
        
        // 3️⃣ CREATE BOOKING
        Booking booking = new Booking();
        booking.setCustomer(customer);
        booking.setVehicle(veh);
       
 

        booking.setSourcelocation(dto.getSourceLoc());
        booking.setDestinationlocation(dto.getDestinationLoc());
        booking.setDistancetravlled(dto.getDistanceTravelled());
        booking.setEstimatedtimerequired(dto.getEstimatedTime());
        
        booking.setFare(dto.getFare());
        booking.setBookingdate(LocalDate.now());

        // *** FIX: Set booking status to BOOKED ***
        booking.setBookingstatus("booked");
        bookingrepo.save(booking);
       
        customer.getBookinglist().add(booking);
//        d.setbooking(booking);
        
        Driver driver = veh.getDriver(); // get driver from vehicle
        if (driver != null) {
            if (driver.getBookinglist() == null)
                driver.setBookinglist(new ArrayList<>());

            driver.getBookinglist().add(booking);
        }
      
        veh.setAvailableStatus("booked");
        
        customerRepo.save(customer);
        vehicleRepo.save(veh);
    
        
      

//        // 4️⃣ CREATE PAYMENT ENTITY
//        Payment payment = new Payment();
//        payment.setCustomer(customer);
//        payment.setVehicle(veh);
//        payment.setBooking(booking);
//        payment.setAmount(dto.getFare());
//
//        // PAYMENT TYPE SHOULD BE e.g., "UPI", "CASH", "CARD"
//        payment.setPaymenttype("not paid"); // since no payment made yet
//
//        booking.setPayement(payment); // correct setter name

        // 5️⃣ UPDATE VEHICLE STATUS
 
        ResponseStructure<Booking> rs = new ResponseStructure<Booking>();
		rs.setStatuscode(HttpStatus.OK.value());
		rs.setMessage("successfully booked");
		rs.setData(booking);
		return rs;
    }
}
