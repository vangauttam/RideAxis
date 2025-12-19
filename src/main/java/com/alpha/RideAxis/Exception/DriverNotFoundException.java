package com.alpha.RideAxis.Exception;

public class DriverNotFoundException extends RuntimeException {
	public DriverNotFoundException() {
        super("Driver not found");
    }
}
