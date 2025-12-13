package com.alpha.RideAxis.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alpha.RideAxis.Entites.Payment;
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer>{

}
