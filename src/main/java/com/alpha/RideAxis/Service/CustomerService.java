package com.alpha.RideAxis.Service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alpha.RideAxis.DTO.RegCustomerDto;
import com.alpha.RideAxis.Entites.Customer;
import com.alpha.RideAxis.Entites.Vehicle;
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

        Customer customer = cr.findByMobileno(mobno);

        if (customer == null) {
            rs.setStatuscode(HttpStatus.NOT_FOUND.value());
            rs.setMessage("Customer not found with mobile number: " + mobno);
            rs.setData(null);
            return rs;
        }

        rs.setStatuscode(HttpStatus.OK.value());
        rs.setMessage("Customer found");
        rs.setData(customer);

        return rs;
    }
//    public List<Vehicle> seeAllAvailableVehicles(long mobno) {
//
//        Customer customer = cr.findByMobileno(mobno);
//
//        if (customer == null) {
//            throw new RuntimeException("Customer not found with mobile number: " + mobno);
//        }
//
//        String customerCity = customer.getCurrentloc();  // ⭐ customer location stored as string
//
//        // Fetch all AVAILABLE vehicles
//        List<Vehicle> allAvailable = vr.findByAvailableStatus("AVAILABLE");
//
//        // Filter vehicles that match the customer city
//        List<Vehicle> filtered = allAvailable.stream()
//                .filter(v -> v.getCurrentcity() != null &&
//                             v.getCurrentcity().equalsIgnoreCase(customerCity))
//                .toList();
//
//        return filtered;
    }

