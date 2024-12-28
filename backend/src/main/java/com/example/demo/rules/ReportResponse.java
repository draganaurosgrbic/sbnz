package com.example.demo.rules;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReportResponse {

	private ReportUnit rsd;
	private ReportUnit eur;
	private ReportUnit usd;
	private ReportUnit chf;
	private ReportUnit gbp;

}
