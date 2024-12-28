package com.example.demo.rules;

import javax.validation.constraints.NotNull;

import com.example.demo.model.BillType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BillRequest {
	
	@NotNull(message = "Type cannot be null")
	private BillType type;
	private double base;
	private int months;

}
