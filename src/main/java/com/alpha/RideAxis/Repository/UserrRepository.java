package com.alpha.RideAxis.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.alpha.RideAxis.Entites.Userr;

public interface UserrRepository extends JpaRepository<Userr, Long> {
	 boolean existsByMobileno(long mobileno);
}
