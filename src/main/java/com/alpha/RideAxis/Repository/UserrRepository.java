package com.alpha.RideAxis.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.alpha.RideAxis.Entites.Userr;

public interface UserrRepository extends JpaRepository<Userr, Long> {
	Optional<Userr> findByMobileno(long mobileno);
	 boolean existsByMobileno(long mobileno);
}
