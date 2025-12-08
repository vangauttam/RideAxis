package com.alpha.RideAxis.Service;

import com.alpha.RideAxis.Entites.GeoCordinates;
import com.alpha.RideAxis.Entites.GeoCordinates;
import com.alpha.RideAxis.Exception.InvalidDestinationLocationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class GeoLocationService {

    @Value("${locationiq.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    // -----------------------------------------------------
    // STEP 1: VALIDATE DESTINATION & RETURN LAT/LON
    // -----------------------------------------------------
    public GeoCordinates getCordinates(String destination) {

        try {
            String url = "https://us1.locationiq.com/v1/search"
                    + "?key=" + apiKey
                    + "&q=" + destination
                    + "&format=json";

            // API returns a JSON array → Convert to List<Map>
            List<Map<String, Object>> result = restTemplate.getForObject(url, List.class);

            // Validate result
            if (result == null || result.isEmpty()) {
                throw new InvalidDestinationLocationException("Invalid destination: " + destination);
            }

            Map<String, Object> first = result.get(0);

            double lat = Double.parseDouble(first.get("lat").toString());
            double lon = Double.parseDouble(first.get("lon").toString());

            return new GeoCordinates(lat, lon);

        } catch (Exception e) {
            throw new InvalidDestinationLocationException("Invalid destination: " + destination);
        }
    }
 // -----------------------------------------------------
 // STEP 3: Calculate distance between 2 coordinates
 // -----------------------------------------------------
 public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {

     final int R = 6371; // Earth radius in KM

     double dLat = Math.toRadians(lat2 - lat1);
     double dLon = Math.toRadians(lon2 - lon1);

     lat1 = Math.toRadians(lat1);
     lat2 = Math.toRadians(lat2);

     double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
             + Math.sin(dLon / 2) * Math.sin(dLon / 2)
             * Math.cos(lat1) * Math.cos(lat2);

     double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

     return R * c; // Distance in KM
 }

    
}

