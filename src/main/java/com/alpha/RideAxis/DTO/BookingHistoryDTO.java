package com.alpha.RideAxis.DTO;


import java.util.List;

public class BookingHistoryDTO {
	
	  
	List<RideDetailsDTO> history;
	private double totalamount;
	
	
    public BookingHistoryDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public BookingHistoryDTO(List<RideDetailsDTO> history, double totalamount) {
		super();
		this.history = history;
		this.totalamount = totalamount;
	}
	@Override
	public String toString() {
		return "BookingHistoryDTO [history=" + history + ", totalamount=" + totalamount + "]";
	}
	public List<RideDetailsDTO> getHistory() {
		return history;
	}
	public void setHistory(List<RideDetailsDTO> history) {
		this.history = history;
	}
	public double getTotalamount() {
		return totalamount;
	}
	public void setTotalamount(double totalamount) {
		this.totalamount = totalamount;
	}
	
   
    
   

}
