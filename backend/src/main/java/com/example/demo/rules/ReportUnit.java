package com.example.demo.rules;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportUnit {

	private long activeCnt;
	private long closedCnt;	
	private double abortedShare;
	private double baseAvg;	
	private double monthsAvg;	
	private double increaseAvg;	
	private double renewAvg;

}
