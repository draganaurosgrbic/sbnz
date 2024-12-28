package com.example.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.BillType;
import com.example.demo.repository.ExchangeRateRepository;

import lombok.AllArgsConstructor;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class ExchangeRateService {

	private final ExchangeRateRepository exchangeRateRepository;
	
	public double convertToRSD(BillType type, double value) {
		return this.exchangeRateRepository.convertToRSD(type, value);
	}

	public double convertToCurrency(BillType type, double value) {
		return this.exchangeRateRepository.convertToCurrency(type, value);
	}

}
