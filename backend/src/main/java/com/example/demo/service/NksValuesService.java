package com.example.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.BillType;
import com.example.demo.repository.NksValuesRepository;

import lombok.AllArgsConstructor;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class NksValuesService {

	private final NksValuesRepository nksValuesRepository;
	
	public double monthsRate(long level, BillType type) {
		return this.nksValuesRepository.findByLevelAndType(level, type).getMonthsRate();
	}
	
	public double baseRate(long level, BillType type) {
		return this.nksValuesRepository.findByLevelAndType(level, type).getBaseRate();
	}
	
}
