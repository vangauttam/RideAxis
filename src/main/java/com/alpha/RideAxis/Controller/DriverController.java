package com.alpha.RideAxis.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.alpha.RideAxis.DTO.CurrentLocationDTO;
import com.alpha.RideAxis.DTO.RegDriverVehicleDTO;
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



}
