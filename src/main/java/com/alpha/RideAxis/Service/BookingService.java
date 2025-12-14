package com.alpha.RideAxis.Service;

import java.time.LocalDate;

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
    

 
	@Transactional
	public ResponseEntity<ResponseStructure<Booking>> bookVehicle(long mobno, BookingDTO dto) {

        Customer customer = cr.findByMobileno(mobno)
                .orElseThrow(() -> new RuntimeException("Customer not found: " + mobno));
        Vehicle veh = vr.findById(dto.getVehicleId())
                .orElseThrow(() -> new RuntimeException("Vehicle not found with id: " + dto.getVehicleId()));
        Booking booking = new Booking();
        booking.setCustomer(customer);
        booking.setVehicle(veh);
        booking.setSourcelocation(dto.getSourceLoc());
        booking.setDestinationlocation(dto.getDestinationLoc());
        booking.setDistancetravlled(dto.getDistanceTravelled());
        booking.setEstimatedtimerequired(dto.getEstimatedTime());
        booking.setFare(dto.getFare());
        booking.setBookingdate(LocalDate.now());
        booking.setBookingstatus("booked");
        br.save(booking);
       
        customer.getBookinglist().add(booking);
        customer.setActivebookingflag(true);
        cr.save(customer);
		      
        veh.setAvailableStatus("booked");
        
        cr.save(customer);
        vr.save(veh);
        
		Driver driver = veh.getDriver(); // get driver from vehicle
		if (driver != null) {
			driver.setStatus("booked");
		}

		veh.setAvailableStatus("booked");

		cr.save(customer);
		vr.save(veh);

            driver.getBookinglist().add(booking);
       
		ResponseStructure<Booking> rs = new ResponseStructure<Booking>();
		rs.setStatuscode(HttpStatus.OK.value());
		rs.setMessage("successfully booked");
		rs.setData(booking);
		return new ResponseEntity<ResponseStructure<Booking>>(rs, HttpStatus.OK);
}
	
	
	public ResponseEntity<ResponseStructure<ActiveBookingDTO>> SeeActiveBooking(long mobno) {

	    Customer customer = cr.findByMobileno(mobno)
	            .orElseThrow(() -> new CustomerNotFoundException("Customer not found with mobile: " + mobno));

	    if (customer.isActivebookingflag()) {

	      
	        Booking booking = br.findActiveBookingByCustomerId(customer.getId());

	        
	        ActiveBookingDTO dto = new ActiveBookingDTO();
	        dto.setCustomername(customer.getName());
	        dto.setCustomermobno(customer.getMobileno());
	        dto.setBooking(booking);
	        dto.setCurrentlocation(booking.getVehicle().getCurrentcity());

	        ResponseStructure<ActiveBookingDTO> rs = new ResponseStructure<>();
	        rs.setStatuscode(HttpStatus.OK.value());
	        rs.setMessage("Active Booking Fetched Successfully");
	        rs.setData(dto);

	        return new ResponseEntity<ResponseStructure<ActiveBookingDTO>>(rs, HttpStatus.OK);

	    } 
	    else {

	        ResponseStructure<ActiveBookingDTO> rs = new ResponseStructure<>();
	        rs.setStatuscode(HttpStatus.OK.value());
	        rs.setMessage("No active booking available for this customer");
	        rs.setData(null);

	        return new ResponseEntity<ResponseStructure<ActiveBookingDTO>>(rs, HttpStatus.OK);
	    }
	}



}
