package com.alpha.RideAxis.DTO;

import java.util.List;

import com.alpha.RideAxis.Entites.Customer;

public class AvailableVehicleDTO {
		private Customer c;
		private int distance;
		private String Sources;
		private String destinaton;
		
		public AvailableVehicleDTO() {
			super();
		}

		public AvailableVehicleDTO(Customer c, int distance, String sources, String destinaton,
				List<VehicleDetailDTO> availableVehicles) {
			super();
			this.c = c;
			this.distance = distance;
			Sources = sources;
			this.destinaton = destinaton;
			this.availableVehicles = availableVehicles;
		}

		private List<VehicleDetailDTO> availableVehicles;

		public Customer getC() {
			return c;
		}

		public void setC(Customer c) {
			this.c = c;
		}

		public int getDistance() {
			return distance;
		}

		public void setDistance(int distance) {
			this.distance = distance;
		}

		public String getSources() {
			return Sources;
		}

		public void setSources(String sources) {
			Sources = sources;
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
			return "AvailableVehicleDTO [c=" + c + ", distance=" + distance + ", Sources=" + Sources + ", destinaton="
					+ destinaton + ", availableVehicles=" + availableVehicles + "]";
		}
}
			