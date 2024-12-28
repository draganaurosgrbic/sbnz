package com.example.demo.dto;

import java.time.LocalDate;

import com.example.demo.model.Transaction;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TransactionDTO {

	private long id;
	private LocalDate date;
	private double amount;

	public TransactionDTO(Transaction transaction) {
		super();
		this.id = transaction.getId();
		this.date = transaction.getDate();
		this.amount = transaction.getAmount();
	}

}
