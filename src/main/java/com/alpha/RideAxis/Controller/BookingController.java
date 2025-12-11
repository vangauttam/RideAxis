package com.alpha.RideAxis.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.alpha.RideAxis.ResponseStructure;
import com.alpha.RideAxis.DTO.ActiveBookingDTO;
import com.alpha.RideAxis.DTO.BookingDTO;
import com.alpha.RideAxis.Entites.Booking;
import com.alpha.RideAxis.Service.BookingService;

@RestController
@RequestMapping("/booking")
public class BookingController {

	@Autowired
	private BookingService bookingService;

	// ----------------------------------------
	// POST: /booking/bookVehicle?mobno=9876543210
	// BODY: BookingDTO JSON
	// ----------------------------------------
	@PostMapping("/bookVehicle")
	public ResponseEntity<ResponseStructure<Booking>> bookVehicle(@RequestParam long mobno,
			@RequestBody BookingDTO dto) {

		return bookingService.bookVehicle(mobno, dto);

	}
	 @GetMapping("/seeactivebooking")
     public ResponseEntity<ResponseStructure<ActiveBookingDTO>> SeeActiveBooking( @RequestParam long mobno){
     	return bookingService.SeeActiveBooking(mobno);
     }
}
