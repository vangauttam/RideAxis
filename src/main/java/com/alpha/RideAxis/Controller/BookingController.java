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

    @PostMapping("/bookVehicle")
    public ResponseEntity<ResponseStructure<Booking>> bookVehicle(
            @RequestParam long mobno,
            @RequestBody BookingDTO dto) {
        return bookingService.bookVehicle(mobno, dto);
    }

    @GetMapping("/seeactivebooking")
    public ResponseEntity<ResponseStructure<ActiveBookingDTO>> seeActiveBooking(
            @RequestParam long mobno) {
        return bookingService.seeActiveBooking(mobno);
    }

    @PutMapping("/cancelride")
    public ResponseEntity<ResponseStructure<Booking>> cancelRideByCustomer(
            @RequestParam int customerId,
            @RequestParam int bookingId) {
        return bookingService.cancelRideByCustomer(customerId, bookingId);
    }

    @PutMapping("/completeride")
    public ResponseEntity<ResponseStructure<Booking>> completeRide(
            @RequestParam int bookingId) {
        return bookingService.completeRide(bookingId);
    }
    @GetMapping("/getotp")
    public ResponseEntity<ResponseStructure<Integer>> getotp(
            @RequestParam int bookingid) {
        return bookingService.getotp(bookingid);
    }
    @PostMapping("/startride")
    public ResponseEntity<ResponseStructure<Booking>> startRide(
            @RequestParam int bookingId,
            @RequestParam int otp) {
        return bookingService.startRide(bookingId, otp);
    }



}
