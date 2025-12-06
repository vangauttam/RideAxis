package com.alpha.RideAxis.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.alpha.RideAxis.DTO.RegCustomerDto;
import com.alpha.RideAxis.Entites.Customer;
import com.alpha.RideAxis.Entites.Vehicle;
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
    public ResponseEntity<ResponseStructure<Customer>> findCustomer(
            @RequestParam long mobno) {

        ResponseStructure<Customer> response = cs.findCustomerByMobile(mobno);

        return ResponseEntity
                .status(response.getStatuscode())
                .body(response);
    }
//    @GetMapping("/seeAllAvailableVehicles")
//    public ResponseEntity<List<Vehicle>> seeAll(@RequestParam long mobno) {
//
//        List<Vehicle> vehicles = cs.seeAllAvailableVehicles(mobno);
//
//        return ResponseEntity.ok(vehicles);
//    }


}
