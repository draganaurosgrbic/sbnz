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

public class TestBase {

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

	public void runAndAssert(double baseUpdate) {
		this.kieSession.insert(this.bill);
		this.kieSession.insert(this.amount);
		this.kieSession.insert(this.response);
		this.kieSession.fireAllRules();
		
		assertTrue(this.response.isValid());
		assertNull(this.response.getMessage());
		assertEquals(this.response.getBaseUpdate(), Double.valueOf(baseUpdate));
		assertNotNull(this.response.getInterestUpdate());
	}
	
	@Test
	public void testRule1() {
		this.bill.setType(BillType.RSD);
		this.bill.setBase(75001);
		this.bill.setBalance(75001);
		this.amount = 75001 * 0.4 + 1;
		this.runAndAssert(0.9 * this.amount);		
	}
	
	@Test
	public void testRule2() {
		this.bill.setType(BillType.RSD);
		this.bill.setBase(300001);
		this.bill.setBalance(300001);
		this.amount = 0.1 * 300001 + 1;
		this.runAndAssert(0.9 * this.amount);
	}

	@Test
	public void testRule3() {
		this.bill.setType(BillType.EUR);
		this.bill.setBase(3001);
		this.bill.setBalance(3001);
		this.amount = 0.1 * 3001 + 1;
		this.runAndAssert(0.9 * this.amount);
	}
	
	@Test
	public void testRule4() {
		this.bill.setType(BillType.RSD);
		this.bill.setBase(75001);
		this.bill.setBalance(75001);
		this.bill.setTransactions(Set.of(ObjectFactory.getTransaction(0.5 * 75001 + 1)));
		this.amount = 0.1 * 75001 + 1;
		this.runAndAssert(0.9 * this.amount);
	}
	
	@Test
	public void testRule5() {
		this.bill.setType(BillType.RSD);
		this.bill.setBase(75001);
		this.bill.setBalance(75001);
		this.amount = 75001 * 0.35 + 1;
		this.runAndAssert(0.8 * this.amount);		
	}
	
	@Test
	public void testRule6() {
		this.bill.setType(BillType.RSD);
		this.bill.setBase(250000);
		this.bill.setBalance(250001);
		this.amount = 0.1 * 250001 + 1;
		this.runAndAssert(0.8 * this.amount);		
	}

	@Test
	public void testRule7() {
		this.bill.setType(BillType.EUR);
		this.bill.setBase(2501);
		this.bill.setBalance(2501);
		this.amount = 0.1 * 2501 + 1;
		this.runAndAssert(0.8 * this.amount);		
	}
	
	@Test
	public void testRule8() {
		this.bill.setType(BillType.RSD);
		this.bill.setBase(75001);
		this.bill.setBalance(75001);
		this.bill.setTransactions(Set.of(ObjectFactory.getTransaction(0.45 * 75001 + 1)));
		this.amount = 0.1 * 75001 + 1;
		this.runAndAssert(0.8 * this.amount);		
	}
	
	@Test
	public void testRule9() {
		this.bill.setType(BillType.RSD);
		this.bill.setBase(75001);
		this.bill.setBalance(75001);
		this.amount = 75001 * 0.3 + 1;
		this.runAndAssert(0.7 * this.amount);		
	}
	
	@Test
	public void testRule10() {
		this.bill.setType(BillType.RSD);
		this.bill.setBase(200001);
		this.bill.setBalance(200001);
		this.amount = 0.1 * 200001 + 1;
		this.runAndAssert(0.7 * this.amount);		
	}

	@Test
	public void testRule11() {
		this.bill.setType(BillType.EUR);
		this.bill.setBase(2001);
		this.bill.setBalance(2001);
		this.amount = 0.1 * 2001 + 1;
		this.runAndAssert(0.7 * this.amount);		
	}
	
	@Test
	public void testRule12() {
		this.bill.setType(BillType.RSD);
		this.bill.setBase(75001);
		this.bill.setBalance(75001);
		this.bill.setTransactions(Set.of(ObjectFactory.getTransaction(0.4 * 75001 + 1)));
		this.amount = 0.1 * 75001 + 1;
		this.runAndAssert(0.7 * this.amount);		
	}
	
	@Test
	public void testRule13() {
		this.bill.setType(BillType.RSD);
		this.bill.setBase(75001);
		this.bill.setBalance(75001);
		this.amount = 75001 * 0.25 + 1;
		this.runAndAssert(0.6 * this.amount);		
	}
	
	@Test
	public void testRule14() {
		this.bill.setType(BillType.RSD);
		this.bill.setBase(150001);
		this.bill.setBalance(150001);
		this.amount = 0.1 * 150001 + 1;
		this.runAndAssert(0.6 * this.amount);		
	}

	@Test
	public void testRule15() {
		this.bill.setType(BillType.EUR);
		this.bill.setBase(1501);
		this.bill.setBalance(1501);
		this.amount = 0.1 * 1501 + 1;
		this.runAndAssert(0.6 * this.amount);		
	}
	
	@Test
	public void testRule16() {
		this.bill.setType(BillType.RSD);
		this.bill.setBase(75001);
		this.bill.setBalance(75001);
		this.bill.setTransactions(Set.of(ObjectFactory.getTransaction(0.35 * 75001 + 1)));
		this.amount = 0.1 * 75001 + 1;
		this.runAndAssert(0.6 * this.amount);		
	}
	
	@Test
	public void testRule17() {
		this.bill.setType(BillType.RSD);
		this.bill.setBase(75001);
		this.bill.setBalance(100);
		this.amount = 100 * 0.2;
		this.runAndAssert(0.5 * this.amount);
	}
	
}
