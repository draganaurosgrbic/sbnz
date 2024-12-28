package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.BillType;
import com.example.demo.model.ExchangeRate;

@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {

	@Query("select ex.rate * :value from ExchangeRate ex where ex.type = :type")
	public double convertToRSD(BillType type, double value);
	
	@Query("select (1 / ex.rate) * :rsdValue from ExchangeRate ex where ex.type = :type")
	public double convertToCurrency(BillType type, double rsdValue);

}
