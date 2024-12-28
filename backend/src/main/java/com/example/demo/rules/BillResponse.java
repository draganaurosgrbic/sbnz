package com.example.demo.rules;

import org.kie.api.definition.type.PropertyReactive;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@PropertyReactive
public class BillResponse {

	private boolean valid;
	private String message;
	private Double nks;
	private Integer points;
	private Double eks;
	private Integer reward;
	private int conditions;
	private int level;
	
	public void setConditions(int level) {
		if (level != this.level) {
			this.conditions = 1;
		}
		else {
			++this.conditions;
		}
		this.level = level;
	}
	
}
