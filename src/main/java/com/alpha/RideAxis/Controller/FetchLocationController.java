package com.alpha.RideAxis.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.alpha.RideAxis.Entites.FetchLocation;
import com.alpha.RideAxis.Repository.FetchLocationRepository;
import com.alpha.RideAxis.ResponseStructure;

@RestController
@RequestMapping("/fetch-location")
public class FetchLocationController {

    @Autowired
    private FetchLocationRepository flr;

    @PostMapping("/save")
    public ResponseStructure<FetchLocation> save(@RequestBody FetchLocation fl) {

        FetchLocation saved = flr.save(fl);

        ResponseStructure<FetchLocation> rs = new ResponseStructure<>();
        rs.setStatuscode(HttpStatus.CREATED.value());
        rs.setMessage("Location saved");
        rs.setData(saved);

        return rs;
    }
}
