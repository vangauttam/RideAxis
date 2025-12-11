package com.alpha.RideAxis.DTO;

import com.alpha.RideAxis.Entites.Booking;

public class ActiveBookingDTO {
	private String customername;
	private long customermobno;
	private Booking booking;
	private String currentlocation;
	public String getCustomername() {
		return customername;
	}
	public void setCustomername(String customername) {
		this.customername = customername;
	}
	public long getCustomermobno() {
		return customermobno;
	}
	public void setCustomermobno(long customermobno) {
		this.customermobno = customermobno;
	}
	public Booking getBooking() {
		return booking;
	}
	public void setBooking(Booking booking) {
		this.booking = booking;
	}
	public String getCurrentlocation() {
		return currentlocation;
	}
	public void setCurrentlocation(String currentlocation) {
		this.currentlocation = currentlocation;
	}
	@Override
	public String toString() {
		return "ActiveBookingDTO [customername=" + customername + ", customermobno=" + customermobno + ", booking="
				+ booking + ", currentlocation=" + currentlocation + "]";
	}
	public ActiveBookingDTO(String customername, long customermobno, Booking booking, String currentlocation) {
		super();
		this.customername = customername;
		this.customermobno = customermobno;
		this.booking = booking;
		this.currentlocation = currentlocation;
	}
	public ActiveBookingDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

}
