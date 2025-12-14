package com.alpha.RideAxis.Controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.alpha.RideAxis.DTO.CurrentLocationDTO;
import com.alpha.RideAxis.DTO.RegDriverVehicleDTO;
import com.alpha.RideAxis.DTO.RideCompletionDTO;
import com.alpha.RideAxis.DTO.BookingHistoryDTO;
import com.alpha.RideAxis.ResponseStructure;
import com.alpha.RideAxis.Entites.Driver;
import com.alpha.RideAxis.Entites.Vehicle;
import com.alpha.RideAxis.Service.DriverService;

@RestController
@RequestMapping("/drivers")
public class DriverController {

    @Autowired
    private DriverService ds;

    //SAVE
    @PostMapping("/register")
    public ResponseEntity<ResponseStructure<Driver>> registerDriverWithVehicle(
            @RequestBody RegDriverVehicleDTO dto) {

        ResponseStructure<Driver> response = ds.registerDriverWithVehicle(dto);

        return ResponseEntity
                .status(response.getStatuscode())
                .body(response);
    }
    @PutMapping("/{driverId}/currentcity")
    public ResponseEntity<ResponseStructure<Vehicle>> updateCurrentCity(
            @PathVariable long driverId,
            @RequestBody CurrentLocationDTO dto) {

        ResponseStructure<Vehicle> response = ds.updateCurrentCity(driverId, dto);
        return ResponseEntity.status(response.getStatuscode()).body(response);
    }
    @GetMapping("/FindDriver")
    public ResponseEntity<ResponseStructure<Driver>> findDriver(
            @RequestParam long mobno) {

        ResponseStructure<Driver> response = ds.findDriverByMobile(mobno);

        return ResponseEntity
                .status(response.getStatuscode())
                .body(response);
    }
    @DeleteMapping("/deleteDriver")
    public ResponseEntity<ResponseStructure<String>> deleteDriver(@RequestParam long mobno) {

        ResponseStructure<String> response = ds.deleteDriverByMobile(mobno);

        return ResponseEntity
                .status(response.getStatuscode())
                .body(response);
    }
    @GetMapping("/bookingHistory")
    public ResponseEntity<ResponseStructure<List<BookingHistoryDTO>>> getDriverHistory(
            @RequestParam long mobno) {

        ResponseStructure<List<BookingHistoryDTO>> response = ds.getDriverBookingHistory(mobno);

        return ResponseEntity.status(response.getStatuscode()).body(response);
    }
    @GetMapping
    public ResponseEntity<?> handlePayment(
                @RequestParam String paytype,
                @RequestParam(required = false) Integer bookingId,
                @RequestParam(required = false) Long paymentId) {

            // CASH PAYMENT
            if (paytype.equalsIgnoreCase("CASH")) {

                if (bookingId == null) {
                    return ResponseEntity.badRequest()
                            .body("bookingId is required for CASH payment");
                }

                return ds.payByCash(bookingId);
            }

            // UPI PAYMENT (Generate URL only)
            if (paytype.equalsIgnoreCase("UPI")) {

                if (bookingId == null) {
                    return ResponseEntity.badRequest()
                            .body("bookingId is required for UPI payment");
                }

                String upiUrl = ds.generateUpiUrl(bookingId);

                ResponseStructure<String> rs = new ResponseStructure<>();
                rs.setStatuscode(HttpStatus.OK.value());
                rs.setMessage("UPI URL generated. Complete payment to confirm.");
                rs.setData(upiUrl);

                return ResponseEntity.ok(rs);
            }

            return ResponseEntity.badRequest()
                    .body("Invalid paytype. Use CASH or UPI");
        }

        // ✅ CONFIRM UPI PAYMENT (separate endpoint)
        @PostMapping("/confirm")
        public ResponseEntity<ResponseStructure<String>> confirmUpiPayment(
                @RequestParam Long paymentId) {

            return ds.confirmUpiPayment(paymentId);
        }
    }







