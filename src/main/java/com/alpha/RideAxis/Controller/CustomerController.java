package com.alpha.RideAxis.Controller;



import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.alpha.RideAxis.DTO.ActiveBookingDTO;
import com.alpha.RideAxis.DTO.AvailableVehicleDTO;

import com.alpha.RideAxis.DTO.RegCustomerDto;
import com.alpha.RideAxis.Entites.Customer;
import com.alpha.RideAxis.ResponseStructure;
import com.alpha.RideAxis.Service.BookingService;
import com.alpha.RideAxis.Service.CustomerService;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService cs;
    @Autowired
    private BookingService bs;
    
 
    

    @PostMapping("/register")
    public ResponseEntity<ResponseStructure<Customer>> registerCustomer(
            @RequestBody RegCustomerDto dto) {

        ResponseStructure<Customer> response = cs.registerCustomer(dto);

        return ResponseEntity
                .status(response.getStatuscode())
                .body(response);
    }
    @DeleteMapping("/deletecustomer")
    public ResponseEntity<ResponseStructure<String>> deleteCustomer(@RequestParam long mobno) {

        ResponseStructure<String> response = cs.deleteCustomerByMobile(mobno);

        return ResponseEntity
                .status(response.getStatuscode())
                .body(response);
    }
  
        @GetMapping("/findcustomer")
        public ResponseStructure<Customer> findCustomerByMobile(
                @RequestParam("mobno") long mobno) {

            return cs.findCustomerByMobile(mobno);
        }
        
        
        @GetMapping("/seeallavailablevehicles")
        public ResponseEntity<ResponseStructure<AvailableVehicleDTO>> seeAllAvailableVehicles(
                @RequestParam("mobno") long mobno,
                @RequestParam("destination") String destination) {

            ResponseStructure<AvailableVehicleDTO> response =
                    cs.seeallAvailableVehicles(mobno, destination);

            return ResponseEntity
                    .status(response.getStatuscode())
                    .body(response);
        }
}
     
