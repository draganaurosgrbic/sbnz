package com.example.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.Transaction;
import com.example.demo.repository.TransactionRepository;

import lombok.AllArgsConstructor;

@Service
@Transactional(readOnly = false)
@AllArgsConstructor
public class TransactionService {

	private final TransactionRepository transactionRepository;
	
	public Transaction save(Transaction transaction) {
		return this.transactionRepository.save(transaction);
	}
	
}
