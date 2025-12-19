package com.alpha.RideAxis.Exception;

public class NoCurrentBookingException extends RuntimeException {
	  public NoCurrentBookingException() {
	        super("No active booking found");
	    }
}
