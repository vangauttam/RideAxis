package com.alpha.RideAxis.Exception;

public class MobileAlreadyRegisteredException extends RuntimeException {
	public MobileAlreadyRegisteredException() {
		super("Mobile number already registered");
	}
}
