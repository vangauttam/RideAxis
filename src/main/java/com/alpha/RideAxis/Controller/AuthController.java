package com.alpha.RideAxis.Controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alpha.RideAxis.ResponseStructure;
import com.alpha.RideAxis.Config.JwtUtils;
import com.alpha.RideAxis.DTO.LoginRequestDTO;
import com.alpha.RideAxis.DTO.RegCustomerDto;
import com.alpha.RideAxis.DTO.RegDriverVehicleDTO;
import com.alpha.RideAxis.Entites.Customer;
import com.alpha.RideAxis.Entites.Userr;
import com.alpha.RideAxis.Entites.Vehicle;
import com.alpha.RideAxis.Entites.Driver;
import com.alpha.RideAxis.Exception.MobileAlreadyRegisteredException;
import com.alpha.RideAxis.Repository.CustomerRepository;
import com.alpha.RideAxis.Repository.DriverRepository;
import com.alpha.RideAxis.Repository.UserrRepository;
import com.alpha.RideAxis.Repository.VehicleRepository;
import com.alpha.RideAxis.Service.CustomerService;

@RestController
@RequestMapping("/auth")
public class AuthController {
	    @Autowired
	    private AuthenticationManager authenticationManager;

	    @Autowired
	    private JwtUtils jwtUtils;

	    @Autowired
	    private CustomerService cs;

	    @Autowired
	    private VehicleRepository vr;

	    private final UserrRepository ur;
	    private final CustomerRepository cr;
	    private final DriverRepository dr;
	    private final PasswordEncoder passwordEncoder;

	    public AuthController(UserrRepository userrRepository,
	                          CustomerRepository customerRepository,
	                          DriverRepository driverRepository,
	                          PasswordEncoder passwordEncoder) {
	        this.ur = userrRepository;
	        this.cr = customerRepository;
	        this.dr = driverRepository;
	        this.passwordEncoder = passwordEncoder;
	    }

	    // =====================================================
	    // CUSTOMER REGISTRATION
	    // =====================================================
	    @PostMapping("/register/customer")
	    public ResponseEntity<ResponseStructure<String>> registerCustomer(
	            @RequestBody RegCustomerDto dto) {

	        long mobileNo = dto.getMobileno();

	        if (ur.existsByMobileno(mobileNo)) {
	            throw new MobileAlreadyRegisteredException();
	        }

	        Userr user = new Userr();
	        user.setMobileno(mobileNo);
	        user.setPassword(passwordEncoder.encode(dto.getPassword()));
	        user.setRole("CUSTOMER");
	        ur.save(user);

	        Customer customer = new Customer();
	        customer.setName(dto.getName());
	        customer.setAge(dto.getAge());
	        customer.setGender(dto.getGender());
	        customer.setMobileno(mobileNo);
	        customer.setEmailid(dto.getEmail());
	        customer.setCurrentloc(
	                cs.getCityFromCoordinates(dto.getLatitude(), dto.getLongitude())
	        );
	        customer.setUserr(user);

	        cr.save(customer);

	        ResponseStructure<String> rs = new ResponseStructure<>();
	        rs.setStatuscode(200);
	        rs.setMessage("Customer registered successfully");
	        rs.setData("SUCCESS");

	        return ResponseEntity.ok(rs);
	    }

	    // =====================================================
	    // DRIVER REGISTRATION
	    // =====================================================
	    @PostMapping("/register/driver")
	    public ResponseEntity<ResponseStructure<String>> registerDriver(
	            @RequestBody RegDriverVehicleDTO dto) {

	        long mobileNo = dto.getMobileno();

	        if (ur.existsByMobileno(mobileNo)) {
	            throw new MobileAlreadyRegisteredException();
	        }

	        Userr userr = new Userr();
	        userr.setMobileno(mobileNo);
	        userr.setPassword(passwordEncoder.encode(dto.getPassword()));
	        userr.setRole("DRIVER");
	        ur.save(userr);

	        Driver driver = new Driver();
	        driver.setDname(dto.getDname());
	        driver.setAge(dto.getAge());
	        driver.setGender(dto.getGender());
	        driver.setMobileno(mobileNo);
	        driver.setMailid(dto.getMailid());
	        driver.setLicenceno(dto.getLicenceno());
	        driver.setUpiid(dto.getUpiid());
	        driver.setStatus("Available");
	        driver.setUserr(userr);

	        dr.save(driver);

	        Vehicle vehicle = new Vehicle();
	        vehicle.setDriver(driver);
	        vehicle.setVname(dto.getVname());
	        vehicle.setVehicleno(dto.getVehicleno());
	        vehicle.setType(dto.getType());
	        vehicle.setModel(dto.getModel());
	        vehicle.setCapacity(dto.getCapacity());
	        vehicle.setCurrentcity(
	                cs.getCityFromCoordinates(dto.getLatitude(), dto.getLongitude())
	        );
	        vehicle.setAvailableStatus("Available");
	        vehicle.setPriceperkm(dto.getPriceperkm());
	        vehicle.setAveragespeed(dto.getAveragespeed());

	        vr.save(vehicle);

	        ResponseStructure<String> rs = new ResponseStructure<>();
	        rs.setStatuscode(200);
	        rs.setMessage("Driver and vehicle registered successfully");
	        rs.setData("SUCCESS");

	        return ResponseEntity.ok(rs);
	    }
	    
	    @PostMapping("/login")
	    public ResponseEntity<ResponseStructure<String>> login(
	            @RequestBody LoginRequestDTO dto) {

	        Userr user = ur.findByMobileno(dto.getMobileNo())
	                .orElseThrow(() -> new RuntimeException("User not found"));

	        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
	            throw new RuntimeException("Invalid credentials");
	        }

	        String token = jwtUtils.generateToken(
	                String.valueOf(user.getMobileno()),
	                user.getRole()
	        );

	        ResponseStructure<String> rs = new ResponseStructure<>();
	        rs.setStatuscode(200);
	        rs.setMessage("Login successful");
	        rs.setData("Bearer " + token);

	        return ResponseEntity.ok(rs);
	    }

	}


