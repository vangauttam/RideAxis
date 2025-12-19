
package com.alpha.RideAxis.DTO;

import com.alpha.RideAxis.Entites.Booking;

public class DriverActiveBookingDTO {

    private String drivername;
    private long drivermobno;
    private Booking booking;
    private String currentlocation;
    

    public DriverActiveBookingDTO() {
		super();
	}

	public DriverActiveBookingDTO(String drivername, long drivermobno, Booking booking, String currentlocation) {
		super();
		this.drivername = drivername;
		this.drivermobno = drivermobno;
		this.booking = booking;
		this.currentlocation = currentlocation;
	}

	@Override
	public String toString() {
		return "DriverActiveBookingDTO [drivername=" + drivername + ", drivermobno=" + drivermobno + ", booking="
				+ booking + ", currentlocation=" + currentlocation + "]";
	}

	public String getDrivername() {
        return drivername;
    }

    public void setDrivername(String drivername) {
        this.drivername = drivername;
    }

    public long getDrivermobno() {
        return drivermobno;
    }

    public void setDrivermobno(long drivermobno) {
        this.drivermobno = drivermobno;
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
}

