package com.example.demo.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Renewal {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	private LocalDate date;

	@NotNull
	private int amount;

	@NotNull
	@ManyToOne
	@JoinColumn
	private Bill bill;

	public Renewal() {
		super();
		this.date = LocalDate.now();
	}
	
	public Renewal(Bill bill, int amount) {
		this();
		this.bill = bill;
		this.amount = amount;
	}

	public long passedMonths() {
		return Math.abs(ChronoUnit.MONTHS.between(this.date, LocalDate.now()));
	}
	
}
