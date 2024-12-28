package com.example.demo.report;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import com.example.demo.ObjectFactory;
import com.example.demo.rules.ReportResponse;
import com.example.demo.model.Bill;
import com.example.demo.model.BillStatus;
import com.example.demo.model.BillType;
import com.example.demo.model.Renewal;
import com.example.demo.model.Transaction;
import com.example.demo.utils.Constants;

public class TestBaseReport {

	private KieSession kieSession;
	
	private List<Bill> bills;
	private ReportResponse response;
		
	@Before
	public void before() {
		KieServices kieService = KieServices.Factory.get();
		KieContainer kieContainer = kieService.newKieContainer(kieService
				.newReleaseId(Constants.KNOWLEDGE_GROUP, Constants.KNOWLEDGE_ATRIFACT, Constants.KNOWLEDGE_VERSION));
		this.kieSession = kieContainer.newKieSession(Constants.REPORT_RULES);
		this.kieSession.getAgenda().getAgendaGroup(Constants.BASE_REPORT).setFocus();

		this.bills = new ArrayList<>();
		this.response = new ReportResponse();
	}

	@After
	public void after() {
		this.kieSession.dispose();
		this.kieSession.destroy();
	}

	@Test
	public void test() {
		Set<Transaction> transactions = Set.of(ObjectFactory.getTransaction(100), ObjectFactory.getTransaction(200));
		Set<Renewal> renewals = Set.of(ObjectFactory.getRenewal(10), ObjectFactory.getRenewal(15));
		
		this.bills.addAll(List.of(
			ObjectFactory.getBill(BillStatus.ACTIVE, BillType.RSD, 100, 10, transactions, renewals), 
			ObjectFactory.getBill(BillStatus.CLOSED, BillType.RSD, 200, 15, transactions, renewals), 
			
			ObjectFactory.getBill(BillStatus.ACTIVE, BillType.EUR, 150, 12, transactions, renewals), 
			ObjectFactory.getBill(BillStatus.ABORTED, BillType.EUR, 300, 18, transactions, renewals), 
			ObjectFactory.getBill(BillStatus.ACTIVE, BillType.EUR, 100, 10, transactions, renewals), 
			
			ObjectFactory.getBill(BillStatus.ACTIVE, BillType.USD, 500, 15, transactions, renewals), 
			
			ObjectFactory.getBill(BillStatus.ACTIVE, BillType.CHF, 200, 10, transactions, renewals), 
			ObjectFactory.getBill(BillStatus.ACTIVE, BillType.CHF, 400, 6, transactions, renewals),
			ObjectFactory.getBill(BillStatus.CLOSED, BillType.CHF, 300, 3, transactions, renewals),
			ObjectFactory.getBill(BillStatus.ABORTED, BillType.CHF, 100, 9, transactions, renewals),
			
			ObjectFactory.getBill(BillStatus.ACTIVE, BillType.GBP, 100, 3, transactions, renewals),
			ObjectFactory.getBill(BillStatus.ACTIVE, BillType.GBP, 120, 5, transactions, renewals),
			ObjectFactory.getBill(BillStatus.CLOSED, BillType.GBP, 150, 6, transactions, renewals),
			ObjectFactory.getBill(BillStatus.ABORTED, BillType.GBP, 180, 2, transactions, renewals),
			ObjectFactory.getBill(BillStatus.ABORTED, BillType.GBP, 200, 10, transactions, renewals)
		));
				
		this.kieSession.insert(this.bills);
		this.kieSession.insert(this.response);
		this.kieSession.fireAllRules();

		assertEquals(this.response.getRsd().getActiveCnt(), 1);
		assertEquals(this.response.getRsd().getClosedCnt(), 1);
		assertEquals(Double.valueOf(this.response.getRsd().getBaseAvg()), Double.valueOf((100 + 200) / 2.0));
		assertEquals(Double.valueOf(this.response.getRsd().getMonthsAvg()), Double.valueOf((10 + 15) / 2.0));
		assertEquals(Double.valueOf(this.response.getRsd().getIncreaseAvg()), Double.valueOf((100 * 2 + 200 * 2) / 4.0));
		assertEquals(Double.valueOf(this.response.getRsd().getRenewAvg()), Double.valueOf((10 * 2 + 15 * 2) / 4.0));
		
		assertEquals(this.response.getEur().getActiveCnt(), 2);
		assertEquals(this.response.getEur().getClosedCnt(), 1);
		assertEquals(Double.valueOf(this.response.getEur().getBaseAvg()), Double.valueOf((150 + 300 + 100) / 3.0));
		assertEquals(Double.valueOf(this.response.getEur().getMonthsAvg()), Double.valueOf((12 + 18 + 10) / 3.0));
		assertEquals(Double.valueOf(this.response.getEur().getIncreaseAvg()), Double.valueOf((100 * 3 + 200 * 3) / 6.0));
		assertEquals(Double.valueOf(this.response.getEur().getRenewAvg()), Double.valueOf((10 * 3 + 15 * 3) / 6.0));

		assertEquals(this.response.getUsd().getActiveCnt(), 1);
		assertEquals(this.response.getUsd().getClosedCnt(), 0);
		assertEquals(Double.valueOf(this.response.getUsd().getBaseAvg()), Double.valueOf(500));
		assertEquals(Double.valueOf(this.response.getUsd().getMonthsAvg()), Double.valueOf(15));
		assertEquals(Double.valueOf(this.response.getUsd().getIncreaseAvg()), Double.valueOf((100 + 200) / 2.0));
		assertEquals(Double.valueOf(this.response.getUsd().getRenewAvg()), Double.valueOf((10 + 15) / 2.0));

		assertEquals(this.response.getChf().getActiveCnt(), 2);
		assertEquals(this.response.getChf().getClosedCnt(), 2);
		assertEquals(Double.valueOf(this.response.getChf().getBaseAvg()), Double.valueOf((400 + 200 + 300 + 100) / 4.0));
		assertEquals(Double.valueOf(this.response.getChf().getMonthsAvg()), Double.valueOf((10 + 6 + 3 + 9) / 4.0));
		assertEquals(Double.valueOf(this.response.getChf().getIncreaseAvg()), Double.valueOf((100 * 4 + 200 * 4) / 8.0));
		assertEquals(Double.valueOf(this.response.getChf().getRenewAvg()), Double.valueOf((10 * 4 + 15 * 4) / 8.0));

		assertEquals(this.response.getGbp().getActiveCnt(), 2);
		assertEquals(this.response.getGbp().getClosedCnt(), 3);
		assertEquals(Double.valueOf(this.response.getGbp().getBaseAvg()), Double.valueOf((100 + 120 + 150 + 180 + 200) / 5.0));
		assertEquals(Double.valueOf(this.response.getGbp().getMonthsAvg()), Double.valueOf((3 + 5 + 6 + 2 + 10) / 5.0));
		assertEquals(Double.valueOf(this.response.getGbp().getIncreaseAvg()), Double.valueOf((100 * 5 + 200 * 5) / 10.0));
		assertEquals(Double.valueOf(this.response.getGbp().getRenewAvg()), Double.valueOf((10 * 5 + 15 * 5) / 10.0));

	}

}
