package com.alpha.RideAxis.DTO;

public class OTPVerificationDTO {
	 private int bookingId;
	    private int otp;
		public int getBookingId() {
			return bookingId;
		}
		public void setBookingId(int bookingId) {
			this.bookingId = bookingId;
		}
		public int getOtp() {
			return otp;
		}
		public void setOtp(int otp) {
			this.otp = otp;
		}
		@Override
		public String toString() {
			return "OTPVerificationDTO [bookingId=" + bookingId + ", otp=" + otp + "]";
		}
		public OTPVerificationDTO(int bookingId, int otp) {
			super();
			this.bookingId = bookingId;
			this.otp = otp;
		}
		public OTPVerificationDTO() {
			super();
			// TODO Auto-generated constructor stub
		}

}
