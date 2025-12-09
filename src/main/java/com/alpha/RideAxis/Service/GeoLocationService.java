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

        String url = "https://us1.locationiq.com/v1/search"
                + "?key=" + apiKey
                + "&q=" + destination
                + "&format=json";

        List<Map<String, Object>> result = restTemplate.getForObject(url, List.class);

        if (result == null || result.isEmpty()) {
            throw new InvalidDestinationLocationException("Invalid destination: " + destination);
        }

        Map<String, Object> first = result.get(0);

      
        String placeClass = (first.get("class") != null) ? first.get("class").toString() : null;
        String placeType  = (first.get("type") != null) ? first.get("type").toString() : null;

      
        List<String> allowedTypes = List.of(
                "city", "town", "village", "hamlet", "suburb", "neighbourhood",
                "locality", "county", "state", "region", "district"
        );

        List<String> allowedClasses = List.of(
                "place", "boundary", "admin"
        );

   
        if (placeClass == null || placeType == null ||
                !allowedClasses.contains(placeClass.toLowerCase()) ||
                !allowedTypes.contains(placeType.toLowerCase())) {

            throw new InvalidDestinationLocationException(
                    "Enter only city/area names, not random words: " + destination
            );
        }

      
        Object latObj = first.get("lat");
        Object lonObj = first.get("lon");

        if (latObj == null || lonObj == null) {
            throw new InvalidDestinationLocationException(
                    "Invalid destination: " + destination + ". Coordinates missing."
            );
        }

        double lat = Double.parseDouble(latObj.toString());
        double lon = Double.parseDouble(lonObj.toString());

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
