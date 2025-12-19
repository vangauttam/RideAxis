package com.alpha.RideAxis.Exception;

public class VehicleNotFoundException extends RuntimeException {
    public VehicleNotFoundException() {
    	  super("No active booking found");
    }
}

