package com.alpha.RideAxis.DTO;

import java.time.LocalDate;

public class BookingHistoryDTO {
	
	    private String sourcelocation;
	    private String destinationlocation;
	    private double fare;
	    private double distancetravlled;
	    private String bookingstatus;       // COMPLETED / CANCELLED / ONGOING
	    private LocalDate bookingdate;
	    private double estimatedtimerequired;
	
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
		public double getDistancetravlled() {
			return distancetravlled;
		}
		public void setDistancetravlled(double distancetravlled) {
			this.distancetravlled = distancetravlled;
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
		public BookingHistoryDTO( String sourcelocation, String destinationlocation, double fare,
				double distancetravlled, String bookingstatus, LocalDate bookingdate, double estimatedtimerequired) {
			super();
			this.sourcelocation = sourcelocation;
			this.destinationlocation = destinationlocation;
			this.fare = fare;
			this.distancetravlled = distancetravlled;
			this.bookingstatus = bookingstatus;
			this.bookingdate = bookingdate;
			this.estimatedtimerequired = estimatedtimerequired;
		}
		public BookingHistoryDTO() {
			super();
			// TODO Auto-generated constructor stub
		}
		@Override
		public String toString() {
			return "BookingHistoryDTO [ sourcelocation=" + sourcelocation
					+ ", destinationlocation=" + destinationlocation + ", fare=" + fare + ", distancetravlled="
					+ distancetravlled + ", bookingstatus=" + bookingstatus + ", bookingdate=" + bookingdate
					+ ", estimatedtimerequired=" + estimatedtimerequired + "]";
		}	    
}
