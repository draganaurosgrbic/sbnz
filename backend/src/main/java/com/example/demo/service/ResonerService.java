package com.example.demo.service;

import java.util.List;

import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Service;

import com.example.demo.rules.BillRequest;
import com.example.demo.rules.BillResponse;
import com.example.demo.rules.CloseResponse;
import com.example.demo.rules.IncreaseResponse;
import com.example.demo.rules.RenewalResponse;
import com.example.demo.rules.ReportResponse;
import com.example.demo.model.Account;
import com.example.demo.model.Bill;
import com.example.demo.utils.Constants;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ResonerService {
	
	private final KieContainer kieContainer;
	private final ExchangeRateService rateService;
	private final NksValuesService nksValuesService;
	
	public BillResponse createBill(Account account, BillRequest request) {
		KieSession kieSession = this.kieContainer.newKieSession(Constants.CREATE_RULES);
		kieSession.getAgenda().getAgendaGroup(Constants.CREATE_RULES).setFocus();
		kieSession.setGlobal("rateService", this.rateService);
		kieSession.setGlobal("nksValuesService", this.nksValuesService);
		BillResponse response = new BillResponse();
		kieSession.insert(account);
		kieSession.insert(request);
		kieSession.insert(response);
		this.run(kieSession);
		return response;
	}
	
	public IncreaseResponse increaseBill(Bill bill, double amount) {
		KieSession kieSession = this.kieContainer.newKieSession(Constants.INCREASE_RULES);
		kieSession.getAgenda().getAgendaGroup(Constants.INCREASE_RULES).setFocus();
		IncreaseResponse response = new IncreaseResponse();		
		kieSession.insert(bill);
		kieSession.insert(amount);
		kieSession.insert(response);
		this.run(kieSession);
		return response;
	}
	
	public RenewalResponse renewBill(Bill bill, int amount) {
		KieSession kieSession = this.kieContainer.newKieSession(Constants.RENEW_RULES);
		kieSession.getAgenda().getAgendaGroup(Constants.RENEW_RULES).setFocus();
		RenewalResponse response = new RenewalResponse();
		kieSession.insert(bill);
		kieSession.insert(amount);
		kieSession.insert(response);
		this.run(kieSession);
		return response;
	}
	
	public CloseResponse closeBill(Bill bill) {
		KieSession kieSession = this.kieContainer.newKieSession(Constants.CLOSE_RULES);
		kieSession.getAgenda().getAgendaGroup(Constants.CLOSE_RULES).setFocus();
		CloseResponse response = new CloseResponse();
		kieSession.insert(bill);
		kieSession.insert(response);
		this.run(kieSession);
		return response;
	}
	
	public ReportResponse baseReport(List<Bill> bills) {
		KieSession kieSession = this.kieContainer.newKieSession(Constants.REPORT_RULES);
		kieSession.getAgenda().getAgendaGroup(Constants.BASE_REPORT).setFocus();
		ReportResponse response = new ReportResponse();
		kieSession.insert(bills);
		kieSession.insert(response);
		this.run(kieSession);
		return response;
	}

	public List<Account> advancedReport(List<Account> accounts, int index){
		String agendaGroup = index == 1 ? Constants.FIRST_REPORT : 
			index == 2 ? Constants.SECOND_REPORT : Constants.THIRD_REPORT;
		KieSession kieSession = this.kieContainer.newKieSession(Constants.REPORT_RULES);
		kieSession.getAgenda().getAgendaGroup(agendaGroup).setFocus();
		kieSession.setGlobal("rateService", this.rateService);
		kieSession.insert(accounts);
		this.run(kieSession);
		return accounts;
	}

	private void run(KieSession kieSession) {
		kieSession.fireAllRules();
		kieSession.dispose();
		kieSession.destroy();		
	}

}
