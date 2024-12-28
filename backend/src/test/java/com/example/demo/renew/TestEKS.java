package com.example.demo.renew;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import com.example.demo.ObjectFactory;
import com.example.demo.rules.RenewalResponse;
import com.example.demo.model.Account;
import com.example.demo.model.Bill;
import com.example.demo.model.BillType;
import com.example.demo.utils.Constants;

public class TestEKS {

	private KieSession kieSession;

	private Account account;
	private Bill bill;
	private int amount;
	private RenewalResponse response;
		
	@Before
	public void before() {
		KieServices kieService = KieServices.Factory.get();
		KieContainer kieContainer = kieService.newKieContainer(kieService
				.newReleaseId(Constants.KNOWLEDGE_GROUP, Constants.KNOWLEDGE_ATRIFACT, Constants.KNOWLEDGE_VERSION));
		this.kieSession = kieContainer.newKieSession(Constants.RENEW_RULES);
		this.kieSession.getAgenda().getAgendaGroup(Constants.RENEW_RULES).setFocus();

		this.account = new Account();
		this.bill = new Bill();
		this.response = new RenewalResponse();
		
		this.account.setBills(Set.of(this.bill));
		this.bill.setAccount(this.account);
	}

	@After
	public void after() {
		this.kieSession.dispose();
		this.kieSession.destroy();
	}

	public void runAndAssert(double interestUpdate) {
		this.kieSession.insert(this.bill);
		this.kieSession.insert(this.amount);
		this.kieSession.insert(this.response);
		this.kieSession.fireAllRules();
		
		assertTrue(this.response.isValid());
		assertNull(this.response.getMessage());
		assertEquals(this.response.getInterestUpdate(), Double.valueOf(interestUpdate));
	}
	
	@Test
	public void testRule1() {
		this.bill.setType(BillType.RSD);
		this.bill.setStartDate(LocalDate.now());
		this.bill.setEndDate(LocalDate.now().plusMonths(23));
		this.bill.setBase(200001);
		this.bill.setInterest(1);
		this.amount = 17;
		this.runAndAssert(0.025 * 1);
	}

	@Test
	public void testRule2() {
		this.bill.setType(BillType.RSD);
		this.bill.setStartDate(LocalDate.now());
		this.bill.setEndDate(LocalDate.now().plusMonths(23));
		this.bill.setBase(270001);
		this.bill.setInterest(1);
		this.amount = 16;
		this.runAndAssert(0.025 * 1);
	}

	@Test
	public void testRule3() {
		this.bill.setType(BillType.EUR);
		this.bill.setStartDate(LocalDate.now());
		this.bill.setEndDate(LocalDate.now().plusMonths(19));
		this.bill.setBase(2701);
		this.bill.setInterest(1);
		this.amount = 16;
		this.runAndAssert(0.025 * 1);
	}

	@Test
	public void testRule4() {
		this.bill.setType(BillType.RSD);
		this.bill.setStartDate(LocalDate.now());
		this.bill.setEndDate(LocalDate.now().plusMonths(23));
		this.bill.setBase(200001);
		this.bill.setInterest(1);
		this.bill.setRenewals(Set.of(ObjectFactory.getRenewal(16)));
		this.amount = 16;
		this.runAndAssert(0.025 * 1);
	}
	
	@Test
	public void testRule5() {
		this.bill.setType(BillType.EUR);
		this.bill.setStartDate(LocalDate.now());
		this.bill.setEndDate(LocalDate.now().plusMonths(19));
		this.bill.setBase(2001);
		this.bill.setInterest(1);
		this.bill.setRenewals(Set.of(ObjectFactory.getRenewal(15)));
		this.amount = 15;
		this.runAndAssert(0.025 * 1);
	}
	
	@Test
	public void testRule6() {
		this.bill.setType(BillType.RSD);
		this.bill.setStartDate(LocalDate.now());
		this.bill.setEndDate(LocalDate.now().plusMonths(23));
		this.bill.setBase(200001);
		this.bill.setInterest(1);
		this.amount = 14;
		this.runAndAssert(0.02 * 1);
	}

	@Test
	public void testRule7() {
		this.bill.setType(BillType.RSD);
		this.bill.setStartDate(LocalDate.now());
		this.bill.setEndDate(LocalDate.now().plusMonths(23));
		this.bill.setBase(220001);
		this.bill.setInterest(1);
		this.amount = 13;
		this.runAndAssert(0.02 * 1);
	}

	@Test
	public void testRule8() {
		this.bill.setType(BillType.EUR);
		this.bill.setStartDate(LocalDate.now());
		this.bill.setEndDate(LocalDate.now().plusMonths(19));
		this.bill.setBase(2201);
		this.bill.setInterest(1);
		this.amount = 13;
		this.runAndAssert(0.02 * 1);
	}

	@Test
	public void testRule9() {
		this.bill.setType(BillType.RSD);
		this.bill.setStartDate(LocalDate.now());
		this.bill.setEndDate(LocalDate.now().plusMonths(23));
		this.bill.setBase(200001);
		this.bill.setInterest(1);
		this.bill.setRenewals(Set.of(ObjectFactory.getRenewal(13)));
		this.amount = 13;
		this.runAndAssert(0.02 * 1);
	}
	
	@Test
	public void testRule10() {
		this.bill.setType(BillType.EUR);
		this.bill.setStartDate(LocalDate.now());
		this.bill.setEndDate(LocalDate.now().plusMonths(19));
		this.bill.setBase(2001);
		this.bill.setInterest(1);
		this.bill.setRenewals(Set.of(ObjectFactory.getRenewal(12)));
		this.amount = 12;
		this.runAndAssert(0.02 * 1);
	}
	
	@Test
	public void testRule11() {
		this.bill.setType(BillType.RSD);
		this.bill.setStartDate(LocalDate.now());
		this.bill.setEndDate(LocalDate.now().plusMonths(23));
		this.bill.setBase(150001);
		this.bill.setInterest(1);
		this.amount = 10;
		this.runAndAssert(0.015 * 1);
	}

	@Test
	public void testRule12() {
		this.bill.setType(BillType.RSD);
		this.bill.setStartDate(LocalDate.now());
		this.bill.setEndDate(LocalDate.now().plusMonths(23));
		this.bill.setBase(170001);
		this.bill.setInterest(1);
		this.amount = 9;
		this.runAndAssert(0.015 * 1);
	}

	@Test
	public void testRule13() {
		this.bill.setType(BillType.EUR);
		this.bill.setStartDate(LocalDate.now());
		this.bill.setEndDate(LocalDate.now().plusMonths(19));
		this.bill.setBase(1701);
		this.bill.setInterest(1);
		this.amount = 9;
		this.runAndAssert(0.015 * 1);
	}

	@Test
	public void testRule14() {
		this.bill.setType(BillType.RSD);
		this.bill.setStartDate(LocalDate.now());
		this.bill.setEndDate(LocalDate.now().plusMonths(23));
		this.bill.setBase(150001);
		this.bill.setInterest(1);
		this.bill.setRenewals(Set.of(ObjectFactory.getRenewal(9)));
		this.amount = 9;
		this.runAndAssert(0.015 * 1);
	}
	
	@Test
	public void testRule15() {
		this.bill.setType(BillType.EUR);
		this.bill.setStartDate(LocalDate.now());
		this.bill.setEndDate(LocalDate.now().plusMonths(19));
		this.bill.setBase(1501);
		this.bill.setInterest(1);
		this.bill.setRenewals(Set.of(ObjectFactory.getRenewal(8)));
		this.amount = 8;
		this.runAndAssert(0.015 * 1);
	}
	
	@Test
	public void testRule16() {
		this.bill.setType(BillType.RSD);
		this.bill.setStartDate(LocalDate.now());
		this.bill.setEndDate(LocalDate.now().plusMonths(23));
		this.bill.setBase(75001);
		this.bill.setInterest(1);
		this.amount = 6;
		this.runAndAssert(0.01 * 1);
	}

	@Test
	public void testRule17() {
		this.bill.setType(BillType.RSD);
		this.bill.setStartDate(LocalDate.now());
		this.bill.setEndDate(LocalDate.now().plusMonths(23));
		this.bill.setBase(120001);
		this.bill.setInterest(1);
		this.amount = 5;
		this.runAndAssert(0.01 * 1);
	}

	@Test
	public void testRule18() {
		this.bill.setType(BillType.EUR);
		this.bill.setStartDate(LocalDate.now());
		this.bill.setEndDate(LocalDate.now().plusMonths(19));
		this.bill.setBase(1201);
		this.bill.setInterest(1);
		this.amount = 5;
		this.runAndAssert(0.01 * 1);
	}

	@Test
	public void testRule19() {
		this.bill.setType(BillType.RSD);
		this.bill.setStartDate(LocalDate.now());
		this.bill.setEndDate(LocalDate.now().plusMonths(23));
		this.bill.setBase(75001);
		this.bill.setInterest(1);
		this.bill.setRenewals(Set.of(ObjectFactory.getRenewal(5)));
		this.amount = 5;
		this.runAndAssert(0.01 * 1);
	}
	
	@Test
	public void testRule20() {
		this.bill.setType(BillType.EUR);
		this.bill.setStartDate(LocalDate.now());
		this.bill.setEndDate(LocalDate.now().plusMonths(19));
		this.bill.setBase(751);
		this.bill.setInterest(1);
		this.bill.setRenewals(Set.of(ObjectFactory.getRenewal(4)));
		this.amount = 4;
		this.runAndAssert(0.01 * 1);
	}
	
	@Test
	public void testRule21() {
		this.bill.setType(BillType.RSD);
		this.bill.setStartDate(LocalDate.now());
		this.bill.setEndDate(LocalDate.now().plusMonths(23));
		this.bill.setBase(75001);
		this.bill.setInterest(1);
		this.amount = 5;
		this.runAndAssert(0.005 * 1);
	}

}
