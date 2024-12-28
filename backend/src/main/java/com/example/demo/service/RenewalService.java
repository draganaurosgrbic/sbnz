package com.example.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.Renewal;
import com.example.demo.repository.RenewalRepository;

import lombok.AllArgsConstructor;

@Service
@Transactional(readOnly = false)
@AllArgsConstructor
public class RenewalService {

	private final RenewalRepository renewalRepository;
	
	public Renewal save(Renewal renewal) {
		return this.renewalRepository.save(renewal);
	}
	
}
