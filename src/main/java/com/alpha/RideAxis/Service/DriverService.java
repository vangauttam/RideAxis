package com.alpha.RideAxis.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.alpha.RideAxis.Entites.Booking;
import com.alpha.RideAxis.Entites.Customer;
import com.alpha.RideAxis.Entites.Driver;
import com.alpha.RideAxis.Entites.FetchLocation;
import com.alpha.RideAxis.Entites.Payment;
import com.alpha.RideAxis.Entites.Userr;
import com.alpha.RideAxis.Entites.Vehicle;
import com.alpha.RideAxis.Exception.BookingNotFoundException;
import com.alpha.RideAxis.Exception.DriverNotFoundException;
import com.alpha.RideAxis.Exception.NoCurrentBookingException;
import com.alpha.RideAxis.Exception.VehicleNotFoundException;
import com.alpha.RideAxis.Repository.BookingRepository;
import com.alpha.RideAxis.Repository.CustomerRepository;
import com.alpha.RideAxis.Repository.DriverRepository;
import com.alpha.RideAxis.Repository.FetchLocationRepository;
import com.alpha.RideAxis.Repository.PaymentRepository;
import com.alpha.RideAxis.Repository.UserrRepository;
import com.alpha.RideAxis.Repository.VehicleRepository;

import jakarta.transaction.Transactional;

import com.alpha.RideAxis.ResponseStructure;
import com.alpha.RideAxis.DTO.ActiveBookingDriverDTO;
import com.alpha.RideAxis.DTO.BookingHistoryDTO;
import com.alpha.RideAxis.DTO.CurrentLocationDTO;
import com.alpha.RideAxis.DTO.RegDriverVehicleDTO;
import com.alpha.RideAxis.DTO.RideCompletionDTO;
import com.alpha.RideAxis.DTO.RideDetailsDTO;

@Service
public class DriverService {


    @Autowired
    private DriverRepository dr;

    @Autowired
    private VehicleRepository vr;
    
    @Autowired
    private FetchLocationRepository flr;
    
    @Autowired
    private BookingRepository br;
    
    @Autowired

    private CustomerRepository cr;
    
    @Autowired
    private PaymentRepository pr;
    @Autowired
    private UserrRepository ur;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
  
    @Autowired
    private RestTemplate restTemplate; 

    @Transactional
    public ResponseStructure<Driver> registerDriverWithVehicle(RegDriverVehicleDTO dto) {

        ResponseStructure<Driver> rs = new ResponseStructure<>();

        // ✅ 1. Check duplicate mobile
        if (ur.existsByMobileno(dto.getMobileno())) {
            rs.setStatuscode(HttpStatus.CONFLICT.value());
            rs.setMessage("Mobile number already registered");
            rs.setData(null);
            return rs;
        }

        // ✅ 2. Create User
        Userr userr = new Userr();
        userr.setMobileno(dto.getMobileno());
        userr.setPassword(passwordEncoder.encode(dto.getPassword()));
        userr.setRole("DRIVER");
        userr = ur.save(userr);

        // ✅ 3. Create Driver
        Driver driver = new Driver();
        driver.setLicenceno(dto.getLicenceno());
        driver.setUpiid(dto.getUpiid());
        driver.setDname(dto.getDname());
        driver.setAge(dto.getAge());
        driver.setMobileno(dto.getMobileno());
        driver.setGender(dto.getGender());
        driver.setMailid(dto.getMailid());
        driver.setStatus("AVAILABLE");

        // 🔥 IMPORTANT: link user
        driver.setUserr(userr);

        // ✅ 4. Create Vehicle
        Vehicle vehicle = new Vehicle();
        vehicle.setVname(dto.getVname());
        vehicle.setVehicleno(dto.getVehicleno());
        vehicle.setType(dto.getType());
        vehicle.setModel(dto.getModel());
        vehicle.setCapacity(dto.getCapacity());
        vehicle.setPriceperkm(dto.getPriceperkm());
        vehicle.setLatitude(dto.getLatitude());
        vehicle.setLongitude(dto.getLongitude());
        vehicle.setAveragespeed(dto.getAveragespeed());
        vehicle.setAvailableStatus("AVAILABLE");

        // ✅ 5. Bi-directional mapping
        driver.setVehicle(vehicle);
        vehicle.setDriver(driver);

        // ✅ 6. Save ONLY driver (cascade saves vehicle)
        driver = dr.save(driver);

        rs.setStatuscode(HttpStatus.CREATED.value());
        rs.setMessage("Driver and Vehicle registered successfully");
        rs.setData(driver);

        return rs;
    }

    @Value("${locationiq.api.key}")
    private String locationApiKey;

    @Value("${locationiq.api.url}")
    private String locationApiUrl;

    public ResponseStructure<Vehicle> updateCurrentCity(long driverId, CurrentLocationDTO dto) {

        ResponseStructure<Vehicle> rs = new ResponseStructure<>();

        Driver driver = dr.findById(driverId).orElse(null);
        if (driver == null) {
            rs.setStatuscode(HttpStatus.NOT_FOUND.value());
            rs.setMessage("Driver not found");
            rs.setData(null);
            return rs;
        }

        Vehicle vehicle = driver.getVehicle();
        if (vehicle == null) {
            rs.setStatuscode(HttpStatus.NOT_FOUND.value());
            rs.setMessage("Vehicle not found for this driver");
            rs.setData(null);
            return rs;
        }

        double lat = dto.getLatitude();
        double lon = dto.getLongitude();

        String city;

    
        FetchLocation cachedLocation =flr.findByLatAndLon(lat, lon);

        if (cachedLocation != null) {

           
            city = cachedLocation.getCity();

        } else {

           
            String url = locationApiUrl + "?key=" + locationApiKey + "&lat=" + lat + "&lon=" + lon + "&format=json";

            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            Map<String, Object> address = (Map<String, Object>) response.get("address");

            city = (String) address.getOrDefault(
                    "city",
                    address.getOrDefault(
                            "town",
                            address.getOrDefault("village", "Unknown")
                    )
            );

            
            FetchLocation fl = new FetchLocation();
            fl.setLat(lat);
            fl.setLon(lon);

            fl.setCity(city);

            flr.save(fl);  
        }

        
        vehicle.setCurrentcity(city);
        vehicle.setLatitude(lat);
        vehicle.setLongitude(lon);
        vr.save(vehicle);

        rs.setStatuscode(HttpStatus.OK.value());
        rs.setMessage("Current city updated successfully");
        rs.setData(vehicle);

        return rs;
    }

  
    public ResponseStructure<Driver> findDriverByMobile(long mobno) {

        Driver driver = dr.findByMobileno(mobno);

        ResponseStructure<Driver> rs = new ResponseStructure<>();

        if (driver == null) {
            rs.setStatuscode(404);
            rs.setMessage("Driver not found for mobile number: " + mobno);
            rs.setData(null);
            return rs;
        }

        rs.setStatuscode(200);
        rs.setMessage("Driver found successfully");
        rs.setData(driver);

        return rs;
    }
    public ResponseStructure<String> deleteDriverByMobile(long mobno) {

        ResponseStructure<String> rs = new ResponseStructure<>();

        Driver driver = dr.findByMobileno(mobno);

        if (driver == null) {
            rs.setStatuscode(404);
            rs.setMessage("Driver not found with mobile number: " + mobno);
            rs.setData(null);
            return rs;
        }

       
        Vehicle vehicle = driver.getVehicle();
        if (vehicle != null) {
            vr.delete(vehicle);
        }

        
        dr.delete(driver);

        rs.setStatuscode(200);
        rs.setMessage("Driver deleted successfully");
        rs.setData("Deleted driver with mobile: " + mobno);

        return rs;
    }
    
    public ResponseStructure<List<BookingHistoryDTO>> getDriverBookingHistory(long mobno) {

        ResponseStructure<List<BookingHistoryDTO>> rs = new ResponseStructure<>();
        
        Driver driver = dr.findByMobileno(mobno);

        if (driver == null) {
            rs.setStatuscode(404);
            rs.setMessage("Driver not found with mobile number: " + mobno);
            rs.setData(null);
            return rs;
        }

        List<Booking> bookings = driver.getBookinglist();

        List<RideDetailsDTO> rddto = new ArrayList<>();
        double totalAmount = 0;

        for (Booking b : bookings) {

            RideDetailsDTO ridedetaildto = new RideDetailsDTO();
            ridedetaildto.setSourceloc(b.getSourcelocation());
            ridedetaildto.setDestinationloc(b.getDestinationlocation());
            ridedetaildto.setFare(b.getFare());
            ridedetaildto.setDistanceTravelled(b.getDistancetravlled());

            totalAmount += b.getFare();
            rddto.add(ridedetaildto);
        }

        BookingHistoryDTO historyDTO = new BookingHistoryDTO();
        historyDTO.setHistory(rddto);
        historyDTO.setTotalamount(totalAmount);

        List<BookingHistoryDTO> responseList = new ArrayList<>();
        responseList.add(historyDTO);

        rs.setStatuscode(200);
        rs.setMessage("Driver Booking History Retrieved Successfully");
        rs.setData(responseList);

        return rs;
    }



    @Transactional
    public ResponseEntity<ResponseStructure<RideCompletionDTO>> payByCash(int bookingId,String paytype) {
    	Booking booking = br.findById(bookingId)
    	        .orElseThrow(() -> new BookingNotFoundException());

        booking.setBookingstatus("COMPLETED");   
        booking.setPaymentstatus("PAID");
        Customer customer = booking.getCustomer();
        customer.setActivebookingflag(false);
       

        Vehicle vehicle = booking.getVehicle();
        vehicle.setAvailableStatus("AVAILABLE");


        Payment payment = new Payment();
        payment.setVehicle(vehicle);
        payment.setCustomer(customer);
        payment.setBooking(booking);
        payment.setAmount(booking.getFare());
        payment.setPaymenttype(paytype);
        cr.save(customer);
        vr.save(vehicle);
        br.save(booking);
        pr.save(payment);
        booking.setPayment(payment);

        RideCompletionDTO ridecompletiondto = new RideCompletionDTO();
        ridecompletiondto.setCustomer(customer);
        ridecompletiondto.setVehicle(vehicle);
        ridecompletiondto.setBooking(booking);
        ridecompletiondto.setPayment(payment);
       
        
        ResponseStructure<RideCompletionDTO>rs = new ResponseStructure<>();
        rs.setMessage("Cash Payment Confirmed");
        rs.setStatuscode(HttpStatus.OK.value());
        rs.setData(ridecompletiondto);

        return new ResponseEntity<ResponseStructure<RideCompletionDTO>>(rs, HttpStatus.OK);
    	
    	
    }
    @Value("${qr.api.url}")
    private String qrApiUrl;

    @Value("${qr.api.size}")
    private String qrSize;
    @Transactional
    public String generateUpiUrl(int bookingId) {

        Booking booking = br.findById(bookingId)
                .orElseThrow(BookingNotFoundException::new);
        
        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setCustomer(booking.getCustomer());
        payment.setVehicle(booking.getVehicle());
        payment.setAmount(booking.getFare());
        payment.setPaymenttype("UPI");

        pr.save(payment);                 
        booking.setPayment(payment);

        
        booking.setPaymentstatus("PENDING");

        br.save(booking);

        String upiUrl = "upi://pay?pa=hariteja.ramasahayam@okaxis"
                + "&pn=RideAxis"
                + "&am=" + booking.getFare()
                + "&cu=INR"
                + "&tn=Ride Payment";

        return upiUrl;

    }

    @Transactional
    public ResponseEntity<ResponseStructure<String>> confirmUpiPayment(Long paymentId) {

        Payment payment = pr.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        Booking booking = payment.getBooking();
        booking.setBookingstatus("COMPLETED");   
        booking.setPaymentstatus("PAID");
        
        Customer customer = booking.getCustomer();
        customer.setActivebookingflag(false);
       

        Vehicle vehicle = booking.getVehicle();
        vehicle.setAvailableStatus("AVAILABLE");

        pr.save(payment);
        br.save(booking);

        ResponseStructure<String> rs = new ResponseStructure<>();
        rs.setMessage("UPI Payment Confirmed");
        rs.setStatuscode(HttpStatus.OK.value());
        rs.setData("PAID");

        return new ResponseEntity<>(rs, HttpStatus.OK);
    }
    
    
    @Transactional
    public ResponseEntity<ResponseStructure<String>> cancelBookingByDriver(
            int bookingId, long driverId) {

        Driver driver = dr.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Driver not found"));

        Booking booking = br.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        LocalDate today = LocalDate.now();

        
        booking.setBookingstatus("CANCELLED");
        br.save(booking);

       
        List<Booking> blist = br.findByDriverIdAndBookingDate(driverId, today);

        int count = 0;
        for (Booking b : blist) {
            if ("CANCELLED".equals(b.getBookingstatus())) {
                count++;
            }
        }

        String message;
        String data;

        
        if (count >= 5) {
            driver.setStatus("blocked");

            Vehicle vehicle = driver.getVehicle();
            vehicle.setAvailableStatus("BLOCKED");

            dr.save(driver);

            message = "Booking cancelled. Driver blocked due to 5 cancellations today";
            data = "CANCELLED_AND_DRIVER_BLOCKED";
        } else {
            message = "Booking cancelled by driver successfully";
            data = "CANCELLED";
        }

        ResponseStructure<String> rs = new ResponseStructure<>();
        rs.setStatuscode(HttpStatus.OK.value());
        rs.setMessage(message);
        rs.setData(data);

        return new ResponseEntity<>(rs, HttpStatus.OK);
    }

    public ResponseEntity<ResponseStructure<ActiveBookingDriverDTO>> seeActiveBooking(long mobileno) {

        
        Driver driver = dr.findByMobileno(mobileno);
        if (driver == null) {
            throw new DriverNotFoundException();
        }

        
        Vehicle vehicle = driver.getVehicle();
        if (vehicle == null) {
            throw new VehicleNotFoundException();
        }

       
        Booking booking = br.findActiveBookingByVehicle(vehicle);
        if (booking == null) {
            throw new NoCurrentBookingException();
        }

       
        ActiveBookingDriverDTO dto = new ActiveBookingDriverDTO();
        dto.setBooking(booking);
        dto.setDrivername(driver.getDname());
        dto.setDrivermobno(driver.getMobileno());
        dto.setCurrentlocation(vehicle.getCurrentcity());

        ResponseStructure<ActiveBookingDriverDTO> rs = new ResponseStructure<>();
        rs.setStatuscode(HttpStatus.OK.value());
        rs.setMessage("Current booking fetched successfully");
        rs.setData(dto);

        return ResponseEntity.ok(rs);
    }

   
}