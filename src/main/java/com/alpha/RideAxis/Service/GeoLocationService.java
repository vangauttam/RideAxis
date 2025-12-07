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
    
}

