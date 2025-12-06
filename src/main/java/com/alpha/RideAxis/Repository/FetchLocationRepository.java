package com.alpha.RideAxis.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alpha.RideAxis.Entites.FetchLocation;

@Repository
public interface FetchLocationRepository extends JpaRepository<FetchLocation, Long> {
	FetchLocation findByLatAndLon(double lat, double lon);
}

