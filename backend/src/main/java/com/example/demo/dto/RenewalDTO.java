package com.example.demo.dto;

import java.time.LocalDate;

import com.example.demo.model.Renewal;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RenewalDTO {

	private long id;
	private LocalDate date;
	private int amount;

	public RenewalDTO(Renewal renewal) {
		super();
		this.id = renewal.getId();
		this.date = renewal.getDate();
		this.amount = renewal.getAmount();
	}

}
