package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Renewal;

@Repository
public interface RenewalRepository extends JpaRepository<Renewal, Long> {

}
