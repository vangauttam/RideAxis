package com.alpha.RideAxis.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import com.alpha.RideAxis.DTO.AvailableVehicleDTO;
import com.alpha.RideAxis.DTO.RegCustomerDto;
import com.alpha.RideAxis.Entites.Customer;
import com.alpha.RideAxis.Entites.Vehicle;
import com.alpha.RideAxis.Exception.InvalidDestinationLocationException;
import com.alpha.RideAxis.ResponseStructure;
import com.alpha.RideAxis.Service.CustomerService;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService cs;
    
 
    

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
