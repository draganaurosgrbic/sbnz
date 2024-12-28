package com.example.demo;

import java.time.LocalDate;
import java.util.Set;

import com.example.demo.model.Bill;
import com.example.demo.model.BillStatus;
import com.example.demo.model.BillType;
import com.example.demo.model.Renewal;
import com.example.demo.model.Transaction;

public class ObjectFactory {

	public static Bill getBill(BillStatus status, BillType type, double base, int months, Set<Transaction> transactions, Set<Renewal> renewals) {
		Bill bill = getBill(type, base, months);
		bill.setStatus(status);
		bill.setTransactions(transactions);
		bill.setRenewals(renewals);
		return bill;
	}

	public static Bill getBill(BillStatus status, BillType type, double base, LocalDate startDate, LocalDate endDate) {
		Bill bill = getBill(type, base, startDate, endDate);
		bill.setStatus(status);
		return bill;
	}

	public static Bill getBill(BillType type, double base, LocalDate startDate, LocalDate endDate, Set<Transaction> transactions) {
		Bill bill = getBill(type, base, startDate, endDate);
		bill.setTransactions(transactions);
		return bill;
	}

	public static Bill getBill(BillType type, double base, LocalDate startDate, LocalDate endDate) {
		Bill bill = getBill(type);
		bill.setBase(base);
		bill.setStartDate(startDate);
		bill.setEndDate(endDate);
		return bill;
	}

	public static Bill getBill(BillType type, double base, int months) {
		Bill bill = getBill(type);
		bill.setBase(base);
		bill.setEndDate(LocalDate.now().plusMonths(months));
		return bill;
	}

	public static Bill getBill(BillStatus status, BillType type) {
		Bill bill = getBill(type);
		bill.setStatus(status);
		return bill;
	}
	
	public static Bill getBill(BillType type) {
		Bill bill = new Bill();
		bill.setType(type);
		bill.setEndDate(LocalDate.now());
		return bill;
	}
	
	public static Bill getBill(double base) {
		Bill bill = new Bill();
		bill.setBase(base);
		bill.setEndDate(LocalDate.now());
		return bill;
	}
	
	public static Transaction getTransaction(Bill bill, double amount) {
		Transaction transaction = new Transaction();
		transaction.setBill(bill);
		transaction.setAmount(amount);
		return transaction;
	}

	public static Transaction getTransaction(double amount) {
		Transaction transaction = new Transaction();
		transaction.setAmount(amount);
		return transaction;
	}

	public static Renewal getRenewal(int amount) {
		Renewal renewal = new Renewal();
		renewal.setAmount(amount);
		return renewal;
	}
	
}
