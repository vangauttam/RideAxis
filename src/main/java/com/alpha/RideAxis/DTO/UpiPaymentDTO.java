package com.alpha.RideAxis.DTO;

import java.util.Arrays;

public class UpiPaymentDTO {
	
	private double fare;
	private byte[] qr;
	public UpiPaymentDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public UpiPaymentDTO(double fare, byte[] qr) {
		super();
		this.fare = fare;
		this.qr = qr;
	}
	@Override
	public String toString() {
		return "RideCompletionByUPIDTO [fare=" + fare + ", qr=" + Arrays.toString(qr) + "]";
	}
	public double getFare() {
		return fare;
	}
	public void setFare(double fare) {
		this.fare = fare;
	}
	public byte[] getQr() {
		return qr;
	}
	public void setQr(byte[] qr) {
		this.qr = qr;
	}

}
