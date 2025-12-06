package com.alpha.RideAxis.Service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alpha.RideAxis.DTO.RegDriverVehicleDTO;
import com.alpha.RideAxis.DTO.CurrentLocationDTO;
import com.alpha.RideAxis.Entites.Driver;
import com.alpha.RideAxis.Entites.FetchLocation;
import com.alpha.RideAxis.Entites.Vehicle;
import com.alpha.RideAxis.Repository.DriverRepository;
import com.alpha.RideAxis.Repository.FetchLocationRepository;
import com.alpha.RideAxis.Repository.VehicleRepository;
import com.alpha.RideAxis.ResponseStructure;

@Service
public class DriverService {

    @Autowired
    private DriverRepository dr;

    @Autowired
    private VehicleRepository vr;
    
    @Autowired
    private FetchLocationRepository flr;
    
    @Autowired
    private RestTemplate restTemplate; 

    public ResponseStructure<Driver> registerDriverWithVehicle(RegDriverVehicleDTO dto) {

        Driver driver = new Driver();
        driver.setLicenceno(dto.getLicenceno());
        driver.setUpiid(dto.getUpiid());
        driver.setDname(dto.getDname());
        driver.setAge(dto.getAge());
        driver.setMobileno(dto.getMobileno());
        driver.setGender(dto.getGender());
        driver.setMailid(dto.getMailid());

        Vehicle vehicle = new Vehicle();
        vehicle.setVname(dto.getVname());
        vehicle.setVehicleno(dto.getVehicleno());
        vehicle.setType(dto.getType());
        vehicle.setModel(dto.getModel());
        vehicle.setCapacity(dto.getCapacity());
        vehicle.setPriceperkm(dto.getPriceperkm()); 
        vehicle.setLatitude(dto.getLatitude());
        vehicle.setLongitude(dto.getLongitude());

        
        
        driver.setVehicle(vehicle);
        vehicle.setDriver(driver);

        driver = dr.save(driver);

        vehicle.setVehicleid(driver.getDriverid());
        vr.save(vehicle);

        ResponseStructure<Driver> rs = new ResponseStructure<>();
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

        //check table
        FetchLocation cachedLocation =flr.findByLatAndLon(lat, lon);

        if (cachedLocation != null) {

            // ✔ Use cached city (NO API CALL)
            city = cachedLocation.getCity();

        } else {

           //API calling
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

            flr.save(fl);  // save new cache entry
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

    // 👉 FIND DRIVER BY MOBILE NUMBER
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

        // First delete vehicle associated with driver
        Vehicle vehicle = driver.getVehicle();
        if (vehicle != null) {
            vr.delete(vehicle);
        }

        // Delete driver
        dr.delete(driver);

        rs.setStatuscode(200);
        rs.setMessage("Driver deleted successfully");
        rs.setData("Deleted driver with mobile: " + mobno);

        return rs;
    }


    
}

    

