package com.alpha.RideAxis.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobelExceptionHandler {

    @ExceptionHandler(VehicleNotFoundException.class)
    public ResponseEntity<?> handleVehicleNotFound(VehicleNotFoundException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("statuscode", 404);
        response.put("message", ex.getMessage());
        response.put("data", null);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DriverNotFoundException.class)
    public ResponseEntity<?> handleDriverNotFound(DriverNotFoundException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("statuscode", 404);
        response.put("message", ex.getMessage());
        response.put("data", null);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(InvalidDestinationLocationException.class)
    public ResponseEntity<?> handleInvalidDestination(InvalidDestinationLocationException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("statuscode", 400);  // BAD REQUEST
        response.put("message", ex.getMessage());
        response.put("data", null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    
    
}
