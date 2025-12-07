package com.alpha.RideAxis.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alpha.RideAxis.DTO.AvailableVehicleDTO;
import com.alpha.RideAxis.DTO.RegCustomerDto;
import com.alpha.RideAxis.DTO.VehicleDetailDTO;
import com.alpha.RideAxis.Entites.Customer;
import com.alpha.RideAxis.Entites.GeoCoordinates;
import com.alpha.RideAxis.Entites.Vehicle;
import com.alpha.RideAxis.Exception.CustomerNotFoundException;
import com.alpha.RideAxis.Exception.InvalidDestinationLocationException;
import com.alpha.RideAxis.Repository.CustomerRepository;
import com.alpha.RideAxis.Repository.VehicleRepository;

import jakarta.transaction.Transactional;


import com.alpha.RideAxis.ResponseStructure;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository cr;
    @Autowired
    private VehicleRepository vr;

    @Value("${locationiq.api.key}")
    private String apiKey;

    @Value("${locationiq.api.url}")
    private String apiUrl;

    @Value("${locationiq.api.format}")
    private String format;
    
    

    @Value("${locationiq.api.search}")   // Use search endpoint for validation
    private String searchApiUrl;

    @Autowired
    private GeoLocationService geoService; // External service for distance/time calculation
    
    
    
    public ResponseStructure<Customer> registerCustomer(RegCustomerDto dto) {

        Customer customer = new Customer();
        customer.setName(dto.getName());
        customer.setAge(dto.getAge());
        customer.setGender(dto.getGender());
        customer.setMobileno(dto.getMobileno());
        customer.setEmailid(dto.getEmail());

        // 🔥 Get City Name Using API
        String city = getCityFromCoordinates(dto.getLatitude(), dto.getLongitude());
        customer.setCurrentloc(city);

        // Save to DB
        customer = cr.save(customer);

        ResponseStructure<Customer> rs = new ResponseStructure<>();
        rs.setStatuscode(HttpStatus.CREATED.value());
        rs.setMessage("Customer registered successfully");
        rs.setData(customer);

        return rs;
    }
    
    


  
    private String getCityFromCoordinates(String lat, String lon) {

        RestTemplate restTemplate = new RestTemplate();

        String url = apiUrl +
                     "?key=" + apiKey +
                     "&lat=" + lat +
                     "&lon=" + lon +
                     "&format=" + format;

        try {
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            Map<String, Object> address = (Map<String, Object>) response.get("address");

            if (address.get("city") != null)
                return address.get("city").toString();

            if (address.get("town") != null)
                return address.get("town").toString();

            if (address.get("village") != null)
                return address.get("village").toString();

            if (address.get("state") != null)
                return address.get("state").toString();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "Unknown Location";
    }
    @Transactional
    public ResponseStructure<String> deleteCustomerByMobile(long mobno) {

        ResponseStructure<String> rs = new ResponseStructure<>();

        if (!cr.existsByMobileno(mobno)) {
            rs.setStatuscode(HttpStatus.NOT_FOUND.value());
            rs.setMessage("Customer not found with mobile number: " + mobno);
            rs.setData(null);
            return rs;
        }

        cr.deleteByMobileno(mobno);

        rs.setStatuscode(HttpStatus.OK.value());
        rs.setMessage("Customer deleted successfully");
        rs.setData("Deleted customer with mobile: " + mobno);

        return rs;
    }
    public ResponseStructure<Customer> findCustomerByMobile(long mobno) {

        ResponseStructure<Customer> rs = new ResponseStructure<>();

        Optional<Customer> optional = cr.findByMobileno(mobno);

        if (optional.isEmpty()) {
            rs.setStatuscode(HttpStatus.NOT_FOUND.value());
            rs.setMessage("Customer not found with mobile number: " + mobno);
            rs.setData(null);
            return rs;
        }

        Customer customer = optional.get();

        rs.setStatuscode(HttpStatus.OK.value());
        rs.setMessage("Customer found");
        rs.setData(customer);

        return rs;
    }
    
    
    
   

    public ResponseStructure<AvailableVehicleDTO> seeallAvailableVehicles(long mobileNumber, String destination) {

        ResponseStructure<AvailableVehicleDTO> rs = new ResponseStructure<>();

        // Step 1: Validate Destination Location
        GeoCoordinates destCoords = geoService.getCoordinates(destination);
        if (destCoords == null)
            throw new InvalidDestinationLocationException("Invalid destination: " + destination);

        // Step 2: Get Customer
        Customer customer = cr.findByMobileno(mobileNumber)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));

        GeoCoordinates sourceCoords =new GeoCoordinates(customer.getLatitude(), customer.getLongitude());

        // Step 3: Distance + Time
        double distance = geoService.calculateDistance(sourceCoords, destCoords);
        double estimatedTime = geoService.getEstimatedTime(sourceCoords, destCoords);

        // Step 4: Get all Available Vehicles in customer current location
        List<Vehicle> vehicles =
                vr.findByAvailableStatus("Available", customer.getCurrentloc());

        List<VehicleDetailDTO> vehicleList = new ArrayList<>();

        for (Vehicle v : vehicles) {

            double fare = v.getPriceperkm() * distance;
            double time = distance / v.getAveragespeed();

            VehicleDetailDTO detail = new VehicleDetailDTO();
            detail.setVehicle(v);
            detail.setFare(fare);
            detail.setEstimatedtime(time);

            vehicleList.add(detail);
        }

        // Step 5: Prepare response DTO
        AvailableVehicleDTO dto = new AvailableVehicleDTO();
        dto.setCustomer(customer);
        dto.setDistance(distance);
        dto.setSource(customer.getCurrentloc());
        dto.setDestinaton(destination);
        dto.setAvailableVehicles(vehicleList);

        // Step 6: Return Response
        rs.setStatuscode(HttpStatus.OK.value());
        rs.setMessage("Available Vehicles Fetched Successfully");
        rs.setData(dto);

        return rs;
    }

 
    }

