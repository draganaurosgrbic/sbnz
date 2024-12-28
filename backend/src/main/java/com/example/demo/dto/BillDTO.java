package com.example.demo.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.example.demo.model.Bill;
import com.example.demo.model.BillStatus;
import com.example.demo.model.BillType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BillDTO {

	private long id;
	private BillStatus status;
	private BillType type;
	private LocalDate startDate;
	private LocalDate endDate;
	private double base;
	private double interest;
	private double balance;
	private List<TransactionDTO> transactions;
	private List<RenewalDTO> renewals;
	
	public BillDTO(Bill bill) {
		super();
		this.id = bill.getId();
		this.status = bill.getStatus();
		this.type = bill.getType();
		this.startDate = bill.getStartDate();
		this.endDate = bill.getEndDate();
		this.base = bill.getBase();
		this.interest = bill.getInterest();
		this.balance = bill.getBalance();
		this.transactions = bill.getTransactions().stream().map(TransactionDTO::new).collect(Collectors.toList());
		this.renewals = bill.getRenewals().stream().map(RenewalDTO::new).collect(Collectors.toList());
	}

}
