package com.example.demo.rules;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RenewalResponse {
	
	private boolean valid;
	private String message;
	private Double interestUpdate;

}
