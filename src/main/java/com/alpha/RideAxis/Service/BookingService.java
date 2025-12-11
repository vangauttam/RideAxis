package com.alpha.RideAxis.Service;

import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alpha.RideAxis.ResponseStructure;
import com.alpha.RideAxis.DTO.ActiveBookingDTO;
import com.alpha.RideAxis.DTO.BookingDTO;

import com.alpha.RideAxis.Entites.Booking;
import com.alpha.RideAxis.Entites.Customer;
import com.alpha.RideAxis.Entites.Driver;
import com.alpha.RideAxis.Entites.Vehicle;
import com.alpha.RideAxis.Exception.CustomerNotFoundException;
import com.alpha.RideAxis.Exception.NoCurrentBookingException;
import com.alpha.RideAxis.Repository.BookingRepository;
import com.alpha.RideAxis.Repository.CustomerRepository;

import com.alpha.RideAxis.Repository.VehicleRepository;

@Service
public class BookingService {

    @Autowired
    private CustomerRepository cr;

    @Autowired
    private VehicleRepository vr;
    @Autowired
    private BookingRepository br;
    private Driver d;
    
 
	@Autowired
	private CustomerRepository customerRepo;

	@Autowired
	private VehicleRepository vehicleRepo;
	@Autowired
	private BookingRepository bookingrepo;
	private Driver d;

	@Transactional
	public ResponseEntity<ResponseStructure<Booking>> bookVehicle(long mobno, BookingDTO dto) {

        // 1️⃣ FIND CUSTOMER BY MOBILE NUMBER
        Customer customer = cr.findByMobileno(mobno)
                .orElseThrow(() -> new RuntimeException("Customer not found: " + mobno));

        // 2️⃣ FIND VEHICLE BY ID
        Vehicle veh = vr.findById(dto.getVehicleId())
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
        br.save(booking);
       
        customer.getBookinglist().add(booking);
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

            driver.getBookinglist().add(booking);
        }
      
        veh.setAvailableStatus("booked");
        
        cr.save(customer);
        vr.save(veh);
    
        
      
		Driver driver = veh.getDriver(); // get driver from vehicle
		if (driver != null) {
			if (driver.getBookinglist() == null)
				driver.setBookinglist(new ArrayList<>());

			driver.getBookinglist().add(booking);
			driver.setStatus("booked");
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
		return new ResponseEntity<ResponseStructure<Booking>>(rs, HttpStatus.OK);
	}
	public ResponseEntity<ResponseStructure<ActiveBookingDTO>> SeeActiveBooking(long mobno) {

        // 1. Fetch customer or throw 404
        Customer customer = customerRepo.findByMobileno(mobno)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with mobile: " + mobno));

        // 2. Fetch active booking for that customer
        Booking booking = bookingrepo.findActiveBookingByCustomerId(customer.getId());
        if (booking == null) {
            throw new NoCurrentBookingException("No active booking found for mobile: " + mobno);
        }

        // 3. Prepare DTO
        ActiveBookingDTO dto = new ActiveBookingDTO();
        dto.setCustomername(customer.getName());
        dto.setCustomermobno(customer.getMobileno());
        dto.setBooking(booking);
        dto.setCurrentlocation(booking.getVehicle().getCurrentcity());

        // 4. Wrap in response structure
        ResponseStructure<ActiveBookingDTO> rs = new ResponseStructure<>();
        rs.setStatuscode(HttpStatus.OK.value());
        rs.setMessage("Active Booking Fetched Successfully");
        rs.setData(dto);

        return new ResponseEntity<ResponseStructure<ActiveBookingDTO>>(rs, HttpStatus.OK);
    }

}
