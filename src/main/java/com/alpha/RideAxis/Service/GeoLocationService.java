package com.alpha.RideAxis.Service;

import com.alpha.RideAxis.Entites.GeoCordinates;
import com.alpha.RideAxis.Exception.InvalidDestinationLocationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class GeoLocationService {

    @Value("${locationiq.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();


    public GeoCordinates validateAndGetCoordinates(String destination) {

        
        try { Thread.sleep(1100); } catch (Exception ignored) {}

        String url = "https://us1.locationiq.com/v1/search?key=" + apiKey +
                "&q=" + destination +
                "&format=json&limit=1&normalizecity=1&dedupe=1";

        Object[] response = restTemplate.getForObject(url, Object[].class);

    
        if (response == null || response.length == 0)
            throw new InvalidDestinationLocationException("Invalid destination: " + destination);

        Map first = (Map) response[0];

        String type = (String) first.get("type");

        if (type == null ||
                !(type.equalsIgnoreCase("city")
                        || type.equalsIgnoreCase("town")
                        || type.equalsIgnoreCase("village")
                        || type.equalsIgnoreCase("administrative"))) {

            throw new InvalidDestinationLocationException(
                    "Invalid destination: " + destination + " (not a valid city)"
            );
        }
        String displayName = (String) first.get("display_name");
        if (displayName == null ||
                !displayName.toLowerCase().contains(destination.toLowerCase())) {

            throw new InvalidDestinationLocationException(
                    "City not found: " + destination
            );
        }

        String latStr = (String) first.get("lat");
        String lonStr = (String) first.get("lon");

        if (latStr == null || lonStr == null)
            throw new InvalidDestinationLocationException("Invalid coordinates for: " + destination);

        double lat = Double.parseDouble(latStr);
        double lon = Double.parseDouble(lonStr);

        return new GeoCordinates(lat, lon);
    }


    public double calculateDistance(double sourceLat, double sourceLon, double destLat, double destLon) {

        final int Radius = 6371; // Earth radius in KM

        double dLat = Math.toRadians(destLat - sourceLat);
        double dLon = Math.toRadians(destLon - sourceLon);

        sourceLat = Math.toRadians(sourceLat);
        destLat = Math.toRadians(destLat);

        double haversine =
                Math.sin(dLat / 2) * Math.sin(dLat / 2)
                        + Math.sin(dLon / 2) * Math.sin(dLon / 2)
                        * Math.cos(sourceLat) * Math.cos(destLat);

        double angularDistance =
                2 * Math.atan2(Math.sqrt(haversine), Math.sqrt(1 - haversine));

        return Radius * angularDistance;
    }
}
