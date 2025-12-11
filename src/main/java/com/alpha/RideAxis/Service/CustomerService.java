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
import com.alpha.RideAxis.Entites.GeoCordinates;
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
    
    @Value("${locationiq.api.search}")
    private String searchApiUrl;

    @Autowired
    private GeoLocationService geoService;

    
    public ResponseStructure<Customer> registerCustomer(RegCustomerDto dto) {

        Customer customer = new Customer();
        customer.setName(dto.getName());
        customer.setAge(dto.getAge());
        customer.setGender(dto.getGender());
        customer.setMobileno(dto.getMobileno());
        customer.setEmailid(dto.getEmail());

        String city = getCityFromCoordinates(dto.getLatitude(), dto.getLongitude());
        customer.setCurrentloc(city);

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

    
    
    public ResponseStructure<AvailableVehicleDTO> seeallAvailableVehicles(long mobno, String destination) {

        ResponseStructure<AvailableVehicleDTO> rs = new ResponseStructure<>();

        Customer cust = cr.findByMobileno(mobno).orElseThrow(() -> new CustomerNotFoundException("Customer not found with mobile: " + mobno));

        // --- ONLY ONE API CALL FOR DESTINATION ---
        GeoCordinates destCoords = geoService.validateAndGetCoordinates(destination);

        // --- ONLY ONE API CALL FOR CUSTOMER SOURCE LOCATION ---
        GeoCordinates sourceCoords = geoService.validateAndGetCoordinates(cust.getCurrentloc());

        double distance = geoService.calculateDistance(
                sourceCoords.getLatitude(),
                sourceCoords.getLongitude(),
                destCoords.getLatitude(),
                destCoords.getLongitude()
        );

        List<Vehicle> vehicles = getVehiclesNearCustomer(cust.getCurrentloc());
        List<VehicleDetailDTO> dtolist = mapVehicleDetails(vehicles, distance);

        AvailableVehicleDTO availablevehicledto = new AvailableVehicleDTO();
        availablevehicledto.setCustomer(cust);
        availablevehicledto.setSource(cust.getCurrentloc());
        availablevehicledto.setDestinaton(destination);
        availablevehicledto.setDistance(distance);
        availablevehicledto.setAvailableVehicles(dtolist);

        rs.setStatuscode(HttpStatus.OK.value());
        rs.setMessage("Available vehicles fetched successfully");
        rs.setData(availablevehicledto);

        return rs;
    }



    private List<Vehicle> getVehiclesNearCustomer(String customerCity) {

        List<Vehicle> allVehicles = vr.findAll();
        List<Vehicle> nearby = new ArrayList<>();

        for (Vehicle vehicle : allVehicles) {
            if (vehicle.getCurrentcity() != null 
                    && vehicle.getCurrentcity().equalsIgnoreCase(customerCity)) {

                nearby.add(vehicle);
            }
        }

        return nearby;
    }



    private List<VehicleDetailDTO> mapVehicleDetails(List<Vehicle> vehicles, double distance) {

        List<VehicleDetailDTO> list = new ArrayList<>();

        for (Vehicle vehicle : vehicles) {

            VehicleDetailDTO vehicledetaildto = new VehicleDetailDTO();

            vehicledetaildto.setVehicle(vehicle);

            double fare = vehicle.getPriceperkm() * distance;
            vehicledetaildto.setFare(fare);

            double estimatedTime = (distance / vehicle.getAveragespeed());
            vehicledetaildto.setEstimatedtime(estimatedTime);

            list.add(vehicledetaildto);
        }

        return list; 
        
    }
}