package com.alpha.RideAxis.Service;

import com.alpha.RideAxis.Entites.GeoCoordinates;
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
    // 1. Get Coordinates from Destination String (NO JACKSON)
    // -----------------------------------------------------
    public GeoCoordinates getCoordinates(String locationName) {

        try {
            String url = "https://us1.locationiq.com/v1/search"
                    + "?key=" + apiKey
                    + "&q=" + locationName
                    + "&format=json";

            // API gives array → Map List
            List<Map<String, Object>> jsonList = restTemplate.getForObject(
                    url, List.class
            );

            if (jsonList == null || jsonList.isEmpty()) {
                throw new InvalidDestinationLocationException(
                        "Invalid location: " + locationName
                );
            }

            Map<String, Object> obj = jsonList.get(0);

            double lat = Double.parseDouble(obj.get("lat").toString());
            double lon = Double.parseDouble(obj.get("lon").toString());

            return new GeoCoordinates(lat, lon);

        } catch (Exception e) {
            throw new InvalidDestinationLocationException(
                    "Invalid Location: " + locationName
            );
        }
    }


    // -----------------------------------------------------
    // 2. Haversine Distance
    // -----------------------------------------------------
    public double calculateDistance(GeoCoordinates src, GeoCoordinates dest) {

        final int R = 6371; // Earth radius

        double lat1 = Math.toRadians(src.getLatitude());
        double lon1 = Math.toRadians(src.getLongitude());
        double lat2 = Math.toRadians(dest.getLatitude());
        double lon2 = Math.toRadians(dest.getLongitude());

        double diffLat = lat2 - lat1;
        double diffLon = lon2 - lon1;

        double a = Math.sin(diffLat / 2) * Math.sin(diffLat / 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.sin(diffLon / 2) * Math.sin(diffLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }


    // -----------------------------------------------------
    // 3. Estimated Time Calculation
    // -----------------------------------------------------
    public double getEstimatedTime(GeoCoordinates src, GeoCoordinates dest) {

        double distance = calculateDistance(src, dest);

        double averageSpeed = 50.0; // Default speed

        return distance / averageSpeed;
    }

}
