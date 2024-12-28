package com.example.demo.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import com.example.demo.rules.BillRequest;
import com.example.demo.rules.BillResponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Bill {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	private BillStatus status;

	@NotNull
	private BillType type;
	
	@NotNull
	private LocalDate startDate;

	@NotNull
	private LocalDate endDate;

	@NotNull
	private double base;

	@NotNull
	private double interest;

	@NotNull
	private double balance;

	@NotNull
	@ManyToOne
	@JoinColumn
	private Account account;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, mappedBy = "bill")
	private Set<Transaction> transactions = new HashSet<>();

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, mappedBy = "bill")
	private Set<Renewal> renewals = new HashSet<>();

	public Bill() {
		super();
		this.status = BillStatus.ACTIVE;
		this.startDate = LocalDate.now();
	}
	
	public Bill(Account account, BillRequest request, BillResponse response, double reward) {
		this();
		this.account = account;
		this.type = request.getType();
		this.base = request.getBase();
		this.endDate = LocalDate.now().plusMonths(request.getMonths());
		this.interest = response.getEks() * response.getNks() / 100.0;
		this.balance = request.getBase() + reward;	
	}
		
	public double close() {
		this.status = BillStatus.ABORTED;
		this.endDate = LocalDate.now();
		return this.base;
	}

	public long months() {
		return Math.abs(ChronoUnit.MONTHS.between(this.startDate, this.endDate));
	}
	
	public long passedMonths() {
		return Math.abs(ChronoUnit.MONTHS.between(this.startDate, LocalDate.now()));
	}

	public double passedTime() {
		return Math.abs((double) ChronoUnit.DAYS.between(this.startDate, LocalDate.now()) / ChronoUnit.DAYS.between(this.startDate, this.endDate));
	}
	
}
