package com.alpha.RideAxis.Service;

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
   
    public GeoCordinates getCordinates(String destination) {

        // 1. FORWARD GEOCODE
        String forwardUrl = "https://us1.locationiq.com/v1/search"
                + "?key=" + apiKey
                + "&q=" + destination
                + "&format=json";

        List<Map<String, Object>> result = restTemplate.getForObject(forwardUrl, List.class);

        if (result == null || result.isEmpty()) {
            throw new InvalidDestinationLocationException("Invalid destination: " + destination);
        }

        Map<String, Object> first = result.get(0);
        double lat = Double.parseDouble(first.get("lat").toString());
        double lon = Double.parseDouble(first.get("lon").toString());

        // 2. REVERSE GEOCODE (VALIDATE CITY)
        String reverseUrl = "https://us1.locationiq.com/v1/reverse"
                + "?key=" + apiKey
                + "&lat=" + lat
                + "&lon=" + lon
                + "&format=json";

        Map<String, Object> reverse = restTemplate.getForObject(reverseUrl, Map.class);

        Map<String, Object> address = (Map<String, Object>) reverse.get("address");

        if (address == null) {
            throw new InvalidDestinationLocationException("Invalid destination: " + destination);
        }

        // Check if reverse geocode provides a valid city/state/town/village
        if (address.get("city") == null &&
            address.get("town") == null &&
            address.get("village") == null &&
            address.get("state") == null) {

            throw new InvalidDestinationLocationException(
                    "Invalid destination: " + destination + ". No valid city information."
            );
        }

        // Valid destination confirmed
        return new GeoCordinates(lat, lon);
    }



   
    public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {

        final int Radius = 6371; // Earth radius in KM

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double haversine = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.sin(dLon / 2) * Math.sin(dLon / 2)
                * Math.cos(lat1) * Math.cos(lat2);

        double angularDistance = 2 * Math.atan2(Math.sqrt(haversine), Math.sqrt(1 - haversine));

        return Radius * angularDistance; 
    }

}
