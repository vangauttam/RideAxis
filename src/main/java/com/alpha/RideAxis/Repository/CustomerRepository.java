package com.alpha.RideAxis.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alpha.RideAxis.Entites.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
	Optional<Customer> findByMobileno(long mobileno);
    void deleteByMobileno(long mobileno);
    boolean existsByMobileno(long mobileno);
}
