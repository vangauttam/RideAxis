package com.alpha.RideAxis.DTO;

import java.util.List;

import com.alpha.RideAxis.Entites.Customer;

public class AvailableVehicleDTO {
		private Customer customer;
		private double distance;
		private String source;
		private String destinaton;
		private List<VehicleDetailDTO> availableVehicles;
		public Customer getCustomer() {
			return customer;
		}
		public void setCustomer(Customer customer) {
			this.customer = customer;
		}
		public double getDistance() {
			return distance;
		}
		public void setDistance(double distance) {
			this.distance = distance;
		}
		public String getSource() {
			return source;
		}
		public void setSource(String source) {
			this.source = source;
		}
		public String getDestinaton() {
			return destinaton;
		}
		public void setDestinaton(String destinaton) {
			this.destinaton = destinaton;
		}
		public List<VehicleDetailDTO> getAvailableVehicles() {
			return availableVehicles;
		}
		public void setAvailableVehicles(List<VehicleDetailDTO> availableVehicles) {
			this.availableVehicles = availableVehicles;
		}
		@Override
		public String toString() {
			return "AvailableVehicleDTO [customer=" + customer + ", distance=" + distance + ", source=" + source
					+ ", destinaton=" + destinaton + ", availableVehicles=" + availableVehicles + "]";
		}
		public AvailableVehicleDTO(Customer customer, double distance, String source, String destinaton,
				List<VehicleDetailDTO> availableVehicles) {
			super();
			this.customer = customer;
			this.distance = distance;
			this.source = source;
			this.destinaton = destinaton;
			this.availableVehicles = availableVehicles;
		}
		public AvailableVehicleDTO() {
			super();
			// TODO Auto-generated constructor stub
		}

		
}

			
