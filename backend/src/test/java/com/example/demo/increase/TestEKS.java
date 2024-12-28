package com.example.demo.increase;

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
import com.example.demo.rules.IncreaseResponse;
import com.example.demo.model.Account;
import com.example.demo.model.Bill;
import com.example.demo.model.BillType;
import com.example.demo.utils.Constants;

public class TestEKS {

	private KieSession kieSession;

	private Account account;
	private Bill bill;
	private double amount;
	private IncreaseResponse response;
		
	@Before
	public void before() {
		KieServices kieService = KieServices.Factory.get();
		KieContainer kieContainer = kieService.newKieContainer(kieService
				.newReleaseId(Constants.KNOWLEDGE_GROUP, Constants.KNOWLEDGE_ATRIFACT, Constants.KNOWLEDGE_VERSION));
		this.kieSession = kieContainer.newKieSession(Constants.INCREASE_RULES);
		this.kieSession.getAgenda().getAgendaGroup(Constants.INCREASE_RULES).setFocus();

		this.account = new Account();
		this.bill = new Bill();
		this.response = new IncreaseResponse();
		
		this.account.setBills(Set.of(this.bill));
		this.bill.setAccount(this.account);
		this.bill.setStartDate(LocalDate.now().minusMonths(3));
		this.bill.setEndDate(LocalDate.now().plusMonths(3).plusDays(1));
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
		assertNotNull(this.response.getBaseUpdate());
		assertEquals(this.response.getInterestUpdate(), Double.valueOf(interestUpdate));
	}

	@Test
	public void testRule1() {
		this.bill.setType(BillType.RSD);
		this.bill.setBase(75001);
		this.bill.setBalance(75001);
		this.bill.setInterest(1);
		this.amount = 0.35 * 75001 + 1;
		this.runAndAssert(0.03 * 1);
	}
	
	@Test
	public void testRule2() {
		this.bill.setType(BillType.RSD);
		this.bill.setBase(330001);
		this.bill.setBalance(330001);
		this.bill.setInterest(1);
		this.amount = 0.2 * 330001;
		this.runAndAssert(0.03 * 1);
	}

	@Test
	public void testRule3() {
		this.bill.setType(BillType.EUR);
		this.bill.setBase(3301);
		this.bill.setBalance(3301);
		this.bill.setInterest(1);
		this.amount = 0.2 * 3301;
		this.runAndAssert(0.03 * 1);
	}
	
	@Test
	public void testRule4() {
		this.account.setBalance(100);
		this.bill.setType(BillType.RSD);
		this.bill.setBase(75001);
		this.bill.setBalance(75001);
		this.bill.setInterest(1);
		this.bill.setTransactions(Set.of(ObjectFactory.getTransaction(0.5 * 100 + 1)));
		this.amount = 0.2 * 75001;
		this.runAndAssert(0.015 * 1);
	}

	@Test
	public void testRule5() {
		this.account.setBalance(100);
		this.bill.setType(BillType.EUR);
		this.bill.setBase(751);
		this.bill.setBalance(751);
		this.bill.setInterest(1);
		this.bill.setTransactions(Set.of(ObjectFactory.getTransaction(0.45 * 100 + 1)));
		this.amount = 0.2 * 751;
		this.runAndAssert(0.015 * 1);
	}

	@Test
	public void testRule6() {
		this.bill.setType(BillType.RSD);
		this.bill.setBase(75001);
		this.bill.setBalance(75001);
		this.bill.setInterest(1);
		this.amount = 0.3 * 75001 + 1;
		this.runAndAssert(0.025 * 1);
	}
	
	@Test
	public void testRule7() {
		this.bill.setType(BillType.RSD);
		this.bill.setBase(270001);
		this.bill.setBalance(270001);
		this.bill.setInterest(1);
		this.amount = 0.2 * 270001;
		this.runAndAssert(0.025 * 1);
	}

	@Test
	public void testRule8() {
		this.bill.setType(BillType.EUR);
		this.bill.setBase(2701);
		this.bill.setBalance(2701);
		this.bill.setInterest(1);
		this.amount = 0.2 * 2701;
		this.runAndAssert(0.025 * 1);
	}

	@Test
	public void testRule9() {
		this.account.setBalance(100);
		this.bill.setType(BillType.RSD);
		this.bill.setBase(75001);
		this.bill.setBalance(75001);
		this.bill.setInterest(1);
		this.bill.setTransactions(Set.of(ObjectFactory.getTransaction(0.45 * 100 + 1)));
		this.amount = 0.2 * 75001;
		this.runAndAssert(0.015 * 1);
	}

	@Test
	public void testRule10() {
		this.account.setBalance(100);
		this.bill.setType(BillType.EUR);
		this.bill.setBase(751);
		this.bill.setBalance(751);
		this.bill.setInterest(1);
		this.bill.setTransactions(Set.of(ObjectFactory.getTransaction(0.4 * 100 + 1)));
		this.amount = 0.2 * 751;
		this.runAndAssert(0.015 * 1);
	}

	@Test
	public void testRule11() {
		this.bill.setType(BillType.RSD);
		this.bill.setBase(75001);
		this.bill.setBalance(75001);
		this.bill.setInterest(1);
		this.amount = 0.25 * 75001 + 1;
		this.runAndAssert(0.02 * 1);
	}
	
	@Test
	public void testRule12() {
		this.bill.setType(BillType.RSD);
		this.bill.setBase(220001);
		this.bill.setBalance(220001);
		this.bill.setInterest(1);
		this.amount = 0.2 * 220001;
		this.runAndAssert(0.02 * 1);
	}

	@Test
	public void testRule13() {
		this.bill.setType(BillType.EUR);
		this.bill.setBase(2201);
		this.bill.setBalance(2201);
		this.bill.setInterest(1);
		this.amount = 0.2 * 2201;
		this.runAndAssert(0.02 * 1);
	}
	
	@Test
	public void testRule14() {
		this.account.setBalance(100);
		this.bill.setType(BillType.RSD);
		this.bill.setBase(75001);
		this.bill.setBalance(75001);
		this.bill.setInterest(1);
		this.bill.setTransactions(Set.of(ObjectFactory.getTransaction(0.4 * 100 + 1)));
		this.amount = 0.2 * 75001;
		this.runAndAssert(0.015 * 1);
	}

	@Test
	public void testRule15() {
		this.account.setBalance(100);
		this.bill.setType(BillType.EUR);
		this.bill.setBase(751);
		this.bill.setBalance(751);
		this.bill.setInterest(1);
		this.bill.setTransactions(Set.of(ObjectFactory.getTransaction(0.35 * 100 + 1)));
		this.amount = 0.2 * 751;
		this.runAndAssert(0.015 * 1);
	}
	
	@Test
	public void testRule16() {
		this.bill.setType(BillType.RSD);
		this.bill.setBase(75001);
		this.bill.setBalance(75001);
		this.bill.setInterest(1);
		this.amount = 0.2 * 75001 + 1;
		this.runAndAssert(0.015 * 1);
	}
	
	@Test
	public void testRule17() {
		this.bill.setType(BillType.RSD);
		this.bill.setBase(180001);
		this.bill.setBalance(180001);
		this.bill.setInterest(1);
		this.amount = 0.2 * 180001;
		this.runAndAssert(0.015 * 1);
	}

	@Test
	public void testRule18() {
		this.bill.setType(BillType.EUR);
		this.bill.setBase(1801);
		this.bill.setBalance(1801);
		this.bill.setInterest(1);
		this.amount = 0.2 * 1801;
		this.runAndAssert(0.015 * 1);
	}

	@Test
	public void testRule19() {
		this.account.setBalance(100);
		this.bill.setType(BillType.RSD);
		this.bill.setBase(75001);
		this.bill.setBalance(75001);
		this.bill.setInterest(1);
		this.bill.setTransactions(Set.of(ObjectFactory.getTransaction(0.35 * 100 + 1)));
		this.amount = 0.2 * 75001;
		this.runAndAssert(0.015 * 1);
	}

	@Test
	public void testRule20() {
		this.account.setBalance(100);
		this.bill.setType(BillType.EUR);
		this.bill.setBase(751);
		this.bill.setBalance(751);
		this.bill.setInterest(1);
		this.bill.setTransactions(Set.of(ObjectFactory.getTransaction(0.3 * 100 + 1)));
		this.amount = 0.2 * 751;
		this.runAndAssert(0.015 * 1);
	}
	
	@Test
	public void testRule21() {
		this.bill.setType(BillType.RSD);
		this.bill.setBase(75001);
		this.bill.setInterest(1);
		this.runAndAssert(0.01 * 1);
	}

}
