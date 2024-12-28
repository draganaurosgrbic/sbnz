package com.example.demo.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Account {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	private LocalDate date;

	@NotBlank
	@Pattern(regexp = "[0-9]{13}")
	@Column(unique = true)
	private String jmbg;

	@NotNull
	private LocalDate birthDate;

	@NotBlank
	private String address;

	@NotBlank
	private String city;

	@NotBlank
	private String zipCode;

	@NotNull
	private double balance;

	@NotNull
	@OneToOne(cascade = CascadeType.REMOVE)
	@JoinColumn
	private User user;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, mappedBy = "account")
	private Set<Bill> bills = new HashSet<>();

	public Account() {
		super();
		this.date = LocalDate.now();
	}

	public boolean underage() {
		return Math.abs(ChronoUnit.YEARS.between(this.birthDate, LocalDate.now())) < 18;
	}

	public long passedYears() {
		return Math.abs(ChronoUnit.YEARS.between(this.date, LocalDate.now()));
	}
	
}
