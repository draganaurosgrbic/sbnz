package com.example.demo.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Notification {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	private Date date;

	@NotBlank
	private String message;
	
	@ManyToOne	
	@JoinColumn
	private Account account;

	public Notification() {
		super();
		this.date = new Date();
	}

	public Notification(Account account, String message) {
		this();
		this.account = account;
		this.message = message;
	}
	
}
