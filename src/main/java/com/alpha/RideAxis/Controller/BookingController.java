package com.alpha.RideAxis.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.alpha.RideAxis.DTO.BookingDTO;
import com.alpha.RideAxis.Entites.Booking;
import com.alpha.RideAxis.Service.BookingService;

@RestController
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    // ----------------------------------------
    // POST:  /booking/bookVehicle?mobno=9876543210
    // BODY:  BookingDTO JSON
    // ----------------------------------------
    @PostMapping("/bookVehicle")
    public ResponseEntity<Booking> bookVehicle(
            @RequestParam long mobno,
            @RequestBody BookingDTO dto) {

        Booking booking = bookingService.bookVehicle(mobno, dto);
        return ResponseEntity.ok(booking);
    }
}
