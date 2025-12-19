package com.alpha.RideAxis.Service;

import java.time.LocalDate;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alpha.RideAxis.DTO.ActiveBookingDTO;
import com.alpha.RideAxis.DTO.BookingDTO;
import com.alpha.RideAxis.Entites.Booking;
import com.alpha.RideAxis.Entites.Customer;
import com.alpha.RideAxis.Entites.Driver;
import com.alpha.RideAxis.Entites.Vehicle;
import com.alpha.RideAxis.Exception.BookingNotFoundException;
import com.alpha.RideAxis.Exception.CustomerNotFoundException;
import com.alpha.RideAxis.Repository.BookingRepository;
import com.alpha.RideAxis.Repository.CustomerRepository;
import com.alpha.RideAxis.Repository.VehicleRepository;
import com.alpha.RideAxis.ResponseStructure;

@Service
public class BookingService {

    @Autowired
    private CustomerRepository cr;

    @Autowired
    private VehicleRepository vr;

    @Autowired
    private BookingRepository br;
    @Autowired
    private MailService ms;
   
    private int generateOtp() {
        return 1000 + new Random().nextInt(9000);
    }

    @Transactional
    public ResponseEntity<ResponseStructure<Booking>> bookVehicle(long mobno, BookingDTO dto) {

        Customer customer = cr.findByMobileno(mobno)
                .orElseThrow(() -> new RuntimeException("Customer not found: " + mobno));

        Vehicle veh = vr.findById(dto.getVehicleId())
                .orElseThrow(() -> new RuntimeException("Vehicle not found with id: " + dto.getVehicleId()));

        Booking booking = new Booking();
        booking.setCustomer(customer);
        booking.setVehicle(veh);
        booking.setSourcelocation(dto.getSourceLoc());
        booking.setDestinationlocation(dto.getDestinationLoc());
        booking.setDistancetravlled(dto.getDistanceTravelled());
        booking.setEstimatedtimerequired(dto.getEstimatedTime());
        booking.setFare(dto.getFare());
        booking.setBookingdate(LocalDate.now());
        booking.setBookingstatus("BOOKED");
        booking.setOtp(generateOtp());
        booking.setOtpStage("PICKUP");   
        br.save(booking);

        // Update customer
        customer.getBookinglist().add(booking);
        customer.setActivebookingflag(true);

        // Update vehicle and driver
        veh.setAvailableStatus("BOOKED");
        Driver driver = veh.getDriver();
        if (driver != null) {
            driver.setStatus("BOOKED");
            driver.getBookinglist().add(booking);
        }
        String message =
                "Hello " + customer.getName() + ",\n\n" +
                "Your ride has been successfully booked.\n\n" +
                "Booking ID : " + booking.getId() + "\n" +
                "Vehicle    : " + veh.getVname() + " (" + veh.getVehicleno() + ")\n" +
                "From       : " + booking.getSourcelocation() + "\n" +
                "To         : " + booking.getDestinationlocation() + "\n" +
                "Fare       : ₹" + booking.getFare() + "\n\n" +
                "Thank you for choosing RideAxis.\n" +
                "Have a safe journey 🚕";

        // 4️⃣ SEND MAIL
        ms.sendMail(
                "uttamvanga@gmail.com",
                "RideAxis - Booking Confirmed",
                message
        );

        ResponseStructure<Booking> rs = new ResponseStructure<>();
        rs.setStatuscode(HttpStatus.OK.value());
        rs.setMessage("Vehicle booked successfully");
        rs.setData(booking);
        return ResponseEntity.ok(rs);
    }

    public ResponseEntity<ResponseStructure<ActiveBookingDTO>> seeActiveBooking(long mobno) {

        Customer customer = cr.findByMobileno(mobno)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with mobile: " + mobno));

        if (!customer.isActivebookingflag()) {
            ResponseStructure<ActiveBookingDTO> rs = new ResponseStructure<>();
            rs.setStatuscode(HttpStatus.OK.value());
            rs.setMessage("No active booking available for this customer");
            rs.setData(null);
            return ResponseEntity.ok(rs);
        }

        Booking booking = br.findActiveBookingByCustomerId((int) customer.getId());
        if (booking == null) {
            ResponseStructure<ActiveBookingDTO> rs = new ResponseStructure<>();
            rs.setStatuscode(HttpStatus.OK.value());
            rs.setMessage("No active booking found");
            rs.setData(null);
            return ResponseEntity.ok(rs);
        }

        ActiveBookingDTO dto = new ActiveBookingDTO();
        dto.setCustomername(customer.getName());
        dto.setCustomermobno(customer.getMobileno());
        dto.setBooking(booking);
        dto.setCurrentlocation(booking.getVehicle().getCurrentcity());

        ResponseStructure<ActiveBookingDTO> rs = new ResponseStructure<>();
        rs.setStatuscode(HttpStatus.OK.value());
        rs.setMessage("Active Booking Fetched Successfully");
        rs.setData(dto);

        return ResponseEntity.ok(rs);
    }

    @Transactional
    public ResponseEntity<ResponseStructure<Booking>> cancelRideByCustomer(int customerId, int bookingId) {

        Customer customer = cr.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Booking booking = br.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (booking.getCustomer().getId() != customer.getId()) {
            throw new RuntimeException("Booking does not belong to this customer");
        }


        if ("COMPLETED".equalsIgnoreCase(booking.getBookingstatus())) {
            throw new RuntimeException("Completed ride cannot be cancelled");
        }

        if ("CUSTOMER_CANCELLED".equalsIgnoreCase(booking.getBookingstatus())) {
            throw new RuntimeException("Ride already cancelled");
        }

        double currentRidePenalty = booking.getFare() * 0.10;
        customer.setPenaltyamount(customer.getPenaltyamount() + currentRidePenalty);
        customer.setActivebookingflag(false);

        booking.setBookingstatus("CUSTOMER_CANCELLED");

        Vehicle vehicle = booking.getVehicle();
        vehicle.setAvailableStatus("AVAILABLE");

        ResponseStructure<Booking> rs = new ResponseStructure<>();
        rs.setStatuscode(HttpStatus.OK.value());
        rs.setMessage("Ride cancelled successfully. Penalty applied.");
        rs.setData(booking);

        return ResponseEntity.ok(rs);
    }

    @Transactional
    public ResponseEntity<ResponseStructure<Booking>> completeRide(int bookingId) {

        Booking booking = br.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
     

        booking.setBookingstatus("COMPLETED");

        Customer customer = booking.getCustomer();
        customer.setPenaltyamount(0.0);
        customer.setActivebookingflag(false);

        Vehicle vehicle = booking.getVehicle();
        vehicle.setAvailableStatus("AVAILABLE");

        ResponseStructure<Booking> rs = new ResponseStructure<>();
        rs.setStatuscode(HttpStatus.OK.value());
        rs.setMessage("Ride completed successfully. Penalty reset.");
        rs.setData(booking);

        return ResponseEntity.ok(rs);
    }
    
    public ResponseEntity<ResponseStructure<ActiveBookingDTO>> SeeActiveBooking(long mobno) {

	    Customer customer = cr.findByMobileno(mobno)
	            .orElseThrow(() -> new CustomerNotFoundException("Customer not found with mobile: " + mobno));

	    if (customer.isActivebookingflag()) {

	      
	        Booking booking = br.findActiveBookingByCustomerId(customer.getId());

	        
	        ActiveBookingDTO dto = new ActiveBookingDTO();
	        dto.setCustomername(customer.getName());
	        dto.setCustomermobno(customer.getMobileno());
	        dto.setBooking(booking);
	        dto.setCurrentlocation(booking.getVehicle().getCurrentcity());

	        ResponseStructure<ActiveBookingDTO> rs = new ResponseStructure<>();
	        rs.setStatuscode(HttpStatus.OK.value());
	        rs.setMessage("Active Booking Fetched Successfully");
	        rs.setData(dto);

	        return new ResponseEntity<ResponseStructure<ActiveBookingDTO>>(rs, HttpStatus.OK);

	    } 
	    else {

	        ResponseStructure<ActiveBookingDTO> rs = new ResponseStructure<>();
	        rs.setStatuscode(HttpStatus.OK.value());
	        rs.setMessage("No active booking available for this customer");
	        rs.setData(null);

	        return new ResponseEntity<ResponseStructure<ActiveBookingDTO>>(rs, HttpStatus.OK);
	    }
	    
	    
	}
    
    public ResponseEntity<ResponseStructure<Integer>> getotp(int bookingid) {

        Booking b = br.findById(bookingid)
                .orElseThrow(() -> new BookingNotFoundException());

        if ("COMPLETED".equalsIgnoreCase(b.getBookingstatus())) {
            throw new RuntimeException("OTP not allowed for completed ride");
        }

        ResponseStructure<Integer> rs = new ResponseStructure<>();
        rs.setStatuscode(HttpStatus.OK.value());
        rs.setMessage("OTP sent successfully");
        rs.setData(b.getOtp());

        return ResponseEntity.ok(rs);
    }
    @Transactional
    public ResponseEntity<ResponseStructure<Booking>> startRide(int bookingId, int otp) {

        Booking booking = br.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!"PICKUP".equalsIgnoreCase(booking.getOtpStage())) {
            throw new RuntimeException("Ride already started");
        }

        if (!booking.getOtp().equals(otp)) {
            throw new RuntimeException("Invalid Pickup OTP");
        }

        booking.setBookingstatus("IN_PROGRESS");

        // generate DROP OTP
        booking.setOtp(generateOtp());
        booking.setOtpStage("DROP");

        ResponseStructure<Booking> rs = new ResponseStructure<>();
        rs.setStatuscode(HttpStatus.OK.value());
        rs.setMessage("Ride started successfully");
        rs.setData(booking);

        return ResponseEntity.ok(rs);
    }


}







