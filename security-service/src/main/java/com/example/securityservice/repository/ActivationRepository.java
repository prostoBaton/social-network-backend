package com.example.securityservice.repository;

import com.example.securityservice.model.Activation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ActivationRepository extends JpaRepository<Activation, Integer> {
    Optional<Activation> findByCode(String code);
}
