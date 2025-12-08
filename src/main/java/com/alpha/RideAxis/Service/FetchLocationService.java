package com.alpha.RideAxis.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.alpha.RideAxis.Entites.FetchLocation;
import com.alpha.RideAxis.Repository.FetchLocationRepository;
import com.alpha.RideAxis.ResponseStructure;

@Service
public class FetchLocationService {

    @Autowired
    private FetchLocationRepository flr;

  
    public ResponseStructure<FetchLocation> saveLocation(FetchLocation fl) {

        ResponseStructure<FetchLocation> rs = new ResponseStructure<>();

        FetchLocation existing = flr.findByLatAndLon(fl.getLat(), fl.getLon());
        if (existing != null) {
            rs.setStatuscode(HttpStatus.OK.value());
            rs.setMessage("Location already exists in cache");
            rs.setData(existing);
            return rs;
        }

        flr.save(fl);

        rs.setStatuscode(HttpStatus.CREATED.value());
        rs.setMessage("Location saved successfully");
        rs.setData(fl);

        return rs;
    }
}
