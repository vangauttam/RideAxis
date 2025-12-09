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

        String url = "https://api.openrouteservice.org/geocode/search"
                + "?api_key=" + apiKey
                + "&text=" + destination;

        Map<String, Object> response = restTemplate.getForObject(url, Map.class);

        List<Map<String, Object>> features = (List<Map<String, Object>>) response.get("features");

        if (features == null || features.isEmpty()) {
            throw new InvalidDestinationLocationException("Invalid destination: " + destination);
        }

        Map<String, Object> first = features.get(0);
        Map<String, Object> props = (Map<String, Object>) first.get("properties");

        List<String> validTypes = List.of(
                "locality", "neighbourhood", "city", "town", "village", "suburb", "region"
        );

        String type = props.get("type").toString().toLowerCase();

        if (!validTypes.contains(type)) {
            throw new InvalidDestinationLocationException(
                    "Please enter a valid city/area name only"
            );
        }

        String country = props.get("country").toString();
        if (!country.equalsIgnoreCase("india")) {
            throw new InvalidDestinationLocationException(
                    "Location must be within India only"
            );
        }
        Map<String, Object> geometry = (Map<String, Object>) first.get("geometry");
        List<Object> coordinates = (List<Object>) geometry.get("coordinates");

        double lon = Double.parseDouble(coordinates.get(0).toString());
        double lat = Double.parseDouble(coordinates.get(1).toString());

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
