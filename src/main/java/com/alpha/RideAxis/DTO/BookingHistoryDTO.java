package com.alpha.RideAxis.DTO;

import java.time.LocalDate;

public class BookingHistoryDTO {
	
	  
	private int bookingId;
    private String sourcelocation;
    private String destinationlocation;
    private double fare;
    private double distancetravelled;
    private String bookingstatus;       // COMPLETED / CANCELLED / ONGOING
    private LocalDate bookingdate;
    private double estimatedtimerequired;
   
    
    
    public BookingHistoryDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public BookingHistoryDTO(int bookingId, String sourcelocation, String destinationlocation, double fare,
			double distancetravelled, String bookingstatus, LocalDate bookingdate, double estimatedtimerequired) {
		super();
		this.bookingId = bookingId;
		this.sourcelocation = sourcelocation;
		this.destinationlocation = destinationlocation;
		this.fare = fare;
		this.distancetravelled = distancetravelled;
		this.bookingstatus = bookingstatus;
		this.bookingdate = bookingdate;
		this.estimatedtimerequired = estimatedtimerequired;
	}
	@Override
	public String toString() {
		return "BookingHistoryDTO [bookingId=" + bookingId + ", sourcelocation=" + sourcelocation
				+ ", destinationlocation=" + destinationlocation + ", fare=" + fare + ", distancetravelled="
				+ distancetravelled + ", bookingstatus=" + bookingstatus + ", bookingdate=" + bookingdate
				+ ", estimatedtimerequired=" + estimatedtimerequired + "]";
	}
	public int getBookingId() {
		return bookingId;
	}
	public void setBookingId(int bookingId) {
		this.bookingId = bookingId;
	}
	public String getSourcelocation() {
		return sourcelocation;
	}
	public void setSourcelocation(String sourcelocation) {
		this.sourcelocation = sourcelocation;
	}
	public String getDestinationlocation() {
		return destinationlocation;
	}
	public void setDestinationlocation(String destinationlocation) {
		this.destinationlocation = destinationlocation;
	}
	public double getFare() {
		return fare;
	}
	public void setFare(double fare) {
		this.fare = fare;
	}
	public double getDistancetravelled() {
		return distancetravelled;
	}
	public void setDistancetravelled(double distancetravelled) {
		this.distancetravelled = distancetravelled;
	}
	public String getBookingstatus() {
		return bookingstatus;
	}
	public void setBookingstatus(String bookingstatus) {
		this.bookingstatus = bookingstatus;
	}
	public LocalDate getBookingdate() {
		return bookingdate;
	}
	public void setBookingdate(LocalDate bookingdate) {
		this.bookingdate = bookingdate;
	}
	public double getEstimatedtimerequired() {
		return estimatedtimerequired;
	}
	public void setEstimatedtimerequired(double estimatedtimerequired) {
		this.estimatedtimerequired = estimatedtimerequired;
	}
	
	

}
