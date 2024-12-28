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
import com.example.demo.model.Transaction;
import com.example.demo.utils.Constants;

public class TestValidation {

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
	}

	@After
	public void after() {
		this.kieSession.dispose();
		this.kieSession.destroy();
	}

	public void runAndAssert(String message) {
		this.kieSession.insert(this.bill);
		this.kieSession.insert(this.amount);
		this.kieSession.insert(this.response);
		this.kieSession.fireAllRules();
		
		assertFalse(this.response.isValid());
		assertEquals(this.response.getMessage(), message);
		assertNull(this.response.getBaseUpdate());
		assertNull(this.response.getInterestUpdate());
	}
		
	@Test
	public void testRule1() {
		this.bill.setType(BillType.RSD);
		this.bill.setStartDate(LocalDate.now().minusMonths(1));
		this.bill.setEndDate(LocalDate.now());
		this.runAndAssert("You can't increase bill with passed time more than 95%.");
	}
	
	@Test
	public void testRule2() {
		this.bill.setType(BillType.RSD);
		this.bill.setStartDate(LocalDate.now().minusMonths(1));
		this.bill.setEndDate(LocalDate.now().plusMonths(1));
		this.bill.setBalance(10);
		this.runAndAssert("You can't increase by less than 10% of bill balance.");
	}
	
	@Test
	public void testRule3() {
		this.bill.setType(BillType.RSD);
		this.bill.setStartDate(LocalDate.now().minusMonths(1));
		this.bill.setEndDate(LocalDate.now().plusMonths(1));
		this.bill.setBalance(10);
		this.amount = 6;
		this.runAndAssert("You can't increase by more than 50% of bill balance.");
	}

	@Test
	public void testRule4() {
		this.bill.setType(BillType.RSD);
		this.bill.setStartDate(LocalDate.now().minusMonths(3));
		this.bill.setEndDate(LocalDate.now().plusMonths(3).minusDays(1));
		this.runAndAssert("You can't increase RSD bill with duration less than 6 months.");
	}
	
	@Test
	public void testRule5() {
		this.bill.setType(BillType.RSD);
		this.bill.setStartDate(LocalDate.now().minusMonths(3));
		this.bill.setEndDate(LocalDate.now().plusMonths(3).plusDays(1));
		this.bill.setBase(74999);
		this.runAndAssert("You can't increase RSD bill with base less than 75.000 RSD.");
	}
	
	@Test
	public void testRule6() {
		this.bill.setType(BillType.RSD);
		this.bill.setStartDate(LocalDate.now().minusMonths(3));
		this.bill.setEndDate(LocalDate.now().plusMonths(3).plusDays(1));
		this.bill.setBase(75001);
		this.bill.setTransactions(Set.of(
			new Transaction(), new Transaction(), new Transaction(),
			new Transaction(), new Transaction()
		));
		this.runAndAssert("You can't increase RSD bill that has already been increased at least 5 times.");
	}
	
	@Test
	public void testRule7() {
		this.bill.setType(BillType.EUR);
		this.bill.setStartDate(LocalDate.now().minusMonths(3));
		this.bill.setEndDate(LocalDate.now().plusMonths(1).minusDays(1));
		this.runAndAssert("You can't increase foreign bill with duration less than 4 months.");
	}
	
	@Test
	public void testRule8() {
		this.bill.setType(BillType.EUR);
		this.bill.setStartDate(LocalDate.now().minusMonths(3));
		this.bill.setEndDate(LocalDate.now().plusMonths(1).plusDays(1));
		this.bill.setBase(749);
		this.runAndAssert("You can't increase foreign bill with base less than 750 currency units.");
	}
	
	@Test
	public void testRule9() {
		this.bill.setType(BillType.EUR);
		this.bill.setStartDate(LocalDate.now().minusMonths(3));
		this.bill.setEndDate(LocalDate.now().plusMonths(1).plusDays(1));
		this.bill.setBase(751);
		this.bill.setTransactions(Set.of(
			new Transaction(), new Transaction(), new Transaction()
		));
		this.runAndAssert("You can't increase foreign bill that has already been increased at least 3 times.");
	}
	
	@Test
	public void testRule10() {
		this.bill.setType(BillType.RSD);
		this.bill.setStartDate(LocalDate.now().minusMonths(3));
		this.bill.setEndDate(LocalDate.now().plusMonths(3).plusDays(1));
		this.bill.setBase(200001);
		this.bill.setBalance(100);
		this.amount = 0.4 * 100 + 1;
		this.runAndAssert("You can't increase by more than 40% of balance.");
	}
	
	@Test
	public void testRule11() {
		this.bill.setType(BillType.EUR);
		this.bill.setStartDate(LocalDate.now().minusMonths(3));
		this.bill.setEndDate(LocalDate.now().plusMonths(1).plusDays(1));
		this.bill.setBase(2001);
		this.bill.setBalance(100);
		this.amount = 0.4 * 100 + 1;
		this.runAndAssert("You can't increase by more than 40% of balance.");
	}

	@Test
	public void testRule12() {
		this.bill.setType(BillType.RSD);
		this.bill.setStartDate(LocalDate.now().minusMonths(3));
		this.bill.setEndDate(LocalDate.now().plusMonths(3).plusDays(1));
		this.bill.setBase(75001);
		this.bill.setBalance(100);
		this.account.setBills(Set.of(
			ObjectFactory.getBill(BillType.RSD), ObjectFactory.getBill(BillType.RSD), 
			ObjectFactory.getBill(BillType.RSD), ObjectFactory.getBill(BillType.RSD)
		));
		this.amount = 0.4 * 100 + 1;
		this.runAndAssert("You can't increase by more than 40% of balance.");
	}
	
	@Test
	public void testRule13() {
		this.bill.setType(BillType.EUR);
		this.bill.setStartDate(LocalDate.now().minusMonths(3));
		this.bill.setEndDate(LocalDate.now().plusMonths(1).plusDays(1));
		this.bill.setBase(751);
		this.bill.setBalance(100);
		this.account.setBills(Set.of(
			ObjectFactory.getBill(BillType.EUR), ObjectFactory.getBill(BillType.EUR), 
			ObjectFactory.getBill(BillType.EUR)
		));
		this.amount = 0.4 * 100 + 1;
		this.runAndAssert("You can't increase by more than 40% of balance.");
	}
	
	@Test
	public void testRule14() {
		this.bill.setType(BillType.RSD);
		this.bill.setStartDate(LocalDate.now().minusMonths(3));
		this.bill.setEndDate(LocalDate.now().plusMonths(3).plusDays(1));
		this.bill.setBase(75001);
		this.bill.setBalance(100);
		this.bill.setTransactions(Set.of(ObjectFactory.getTransaction(38000)));
		this.amount = 0.4 * 100 + 1;
		this.runAndAssert("You can't increase by more than 40% of balance.");
	}
	
	@Test
	public void testRule15() {
		this.bill.setType(BillType.EUR);
		this.bill.setStartDate(LocalDate.now().minusMonths(3));
		this.bill.setEndDate(LocalDate.now().plusMonths(1).plusDays(1));
		this.bill.setBase(751);
		this.bill.setBalance(100);
		this.bill.setTransactions(Set.of(ObjectFactory.getTransaction(230)));
		this.amount = 0.4 * 100 + 1;
		this.runAndAssert("You can't increase by more than 40% of balance.");
	}
	
	@Test
	public void testRule16() {
		this.bill.setType(BillType.RSD);
		this.bill.setStartDate(LocalDate.now().minusMonths(3));
		this.bill.setEndDate(LocalDate.now().plusMonths(3).plusDays(1));
		this.bill.setBase(150001);
		this.bill.setBalance(100);
		this.amount = 0.35 * 100 + 1;
		this.runAndAssert("You can't increase by more than 35% of balance.");
	}
	
	@Test
	public void testRule17() {
		this.bill.setType(BillType.EUR);
		this.bill.setStartDate(LocalDate.now().minusMonths(3));
		this.bill.setEndDate(LocalDate.now().plusMonths(1).plusDays(1));
		this.bill.setBase(1501);
		this.bill.setBalance(100);
		this.amount = 0.35 * 100 + 1;
		this.runAndAssert("You can't increase by more than 35% of balance.");
	}
	
	@Test
	public void testRule18() {
		this.bill.setType(BillType.RSD);
		this.bill.setStartDate(LocalDate.now().minusMonths(3));
		this.bill.setEndDate(LocalDate.now().plusMonths(3).plusDays(1));
		this.bill.setBase(75001);
		this.bill.setBalance(100);
		this.account.setBills(Set.of(
			ObjectFactory.getBill(BillType.RSD), ObjectFactory.getBill(BillType.RSD), ObjectFactory.getBill(BillType.RSD), 
			ObjectFactory.getBill(BillType.RSD), ObjectFactory.getBill(BillType.RSD)
		));
		this.amount = 0.35 * 100 + 1;
		this.runAndAssert("You can't increase by more than 35% of balance.");
	}
	
	@Test
	public void testRule19() {
		this.bill.setType(BillType.EUR);
		this.bill.setStartDate(LocalDate.now().minusMonths(3));
		this.bill.setEndDate(LocalDate.now().plusMonths(1).plusDays(1));
		this.bill.setBase(751);
		this.bill.setBalance(100);
		this.account.setBills(Set.of(
			ObjectFactory.getBill(BillType.EUR), ObjectFactory.getBill(BillType.EUR), 
			ObjectFactory.getBill(BillType.EUR), ObjectFactory.getBill(BillType.EUR)
		));
		this.amount = 0.35 * 100 + 1;
		this.runAndAssert("You can't increase by more than 35% of balance.");
	}
	
	@Test
	public void testRule20() {
		this.bill.setType(BillType.RSD);
		this.bill.setStartDate(LocalDate.now().minusMonths(3));
		this.bill.setEndDate(LocalDate.now().plusMonths(3).plusDays(1));
		this.bill.setBase(75001);
		this.bill.setBalance(100);
		this.bill.setTransactions(Set.of(ObjectFactory.getTransaction(46000)));
		this.amount = 0.35 * 100 + 1;
		this.runAndAssert("You can't increase by more than 35% of balance.");
	}
	
	@Test
	public void testRule21() {
		this.bill.setType(BillType.EUR);
		this.bill.setStartDate(LocalDate.now().minusMonths(3));
		this.bill.setEndDate(LocalDate.now().plusMonths(1).plusDays(1));
		this.bill.setBase(751);
		this.bill.setBalance(100);
		this.bill.setTransactions(Set.of(ObjectFactory.getTransaction(310)));
		this.amount = 0.35 * 100 + 1;
		this.runAndAssert("You can't increase by more than 35% of balance.");
	}

	@Test
	public void testRule22() {
		this.bill.setType(BillType.RSD);
		this.bill.setStartDate(LocalDate.now().minusMonths(3));
		this.bill.setEndDate(LocalDate.now().plusMonths(3).plusDays(1));
		this.bill.setBase(100001);
		this.bill.setBalance(100);
		this.amount = 0.3 * 100 + 1;
		this.runAndAssert("You can't increase by more than 30% of balance.");
	}
	
	@Test
	public void testRule23() {
		this.bill.setType(BillType.EUR);
		this.bill.setStartDate(LocalDate.now().minusMonths(3));
		this.bill.setEndDate(LocalDate.now().plusMonths(1).plusDays(1));
		this.bill.setBase(1001);
		this.bill.setBalance(100);
		this.amount = 0.3 * 100 + 1;
		this.runAndAssert("You can't increase by more than 30% of balance.");
	}
	
	@Test
	public void testRule24() {
		this.bill.setType(BillType.RSD);
		this.bill.setStartDate(LocalDate.now().minusMonths(3));
		this.bill.setEndDate(LocalDate.now().plusMonths(3).plusDays(1));
		this.bill.setBase(75001);
		this.bill.setBalance(100);
		this.account.setBills(Set.of(
			ObjectFactory.getBill(BillType.RSD), ObjectFactory.getBill(BillType.RSD), ObjectFactory.getBill(BillType.RSD), 
			ObjectFactory.getBill(BillType.RSD), ObjectFactory.getBill(BillType.RSD), ObjectFactory.getBill(BillType.RSD)
		));
		this.amount = 0.3 * 100 + 1;
		this.runAndAssert("You can't increase by more than 30% of balance.");
	}
	
	@Test
	public void testRule25() {
		this.bill.setType(BillType.EUR);
		this.bill.setStartDate(LocalDate.now().minusMonths(3));
		this.bill.setEndDate(LocalDate.now().plusMonths(1).plusDays(1));
		this.bill.setBase(751);
		this.bill.setBalance(100);
		this.account.setBills(Set.of(
			ObjectFactory.getBill(BillType.EUR), ObjectFactory.getBill(BillType.EUR), ObjectFactory.getBill(BillType.EUR), 
			ObjectFactory.getBill(BillType.EUR), ObjectFactory.getBill(BillType.EUR)
		));
		this.amount = 0.3 * 100 + 1;
		this.runAndAssert("You can't increase by more than 30% of balance.");
	}
	
	@Test
	public void testRule26() {
		this.bill.setType(BillType.RSD);
		this.bill.setStartDate(LocalDate.now().minusMonths(3));
		this.bill.setEndDate(LocalDate.now().plusMonths(3).plusDays(1));
		this.bill.setBase(75001);
		this.bill.setBalance(100);
		this.bill.setTransactions(Set.of(ObjectFactory.getTransaction(53000)));
		this.amount = 0.3 * 100 + 1;
		this.runAndAssert("You can't increase by more than 30% of balance.");
	}
	
	@Test
	public void testRule27() {
		this.bill.setType(BillType.EUR);
		this.bill.setStartDate(LocalDate.now().minusMonths(3));
		this.bill.setEndDate(LocalDate.now().plusMonths(1).plusDays(1));
		this.bill.setBase(751);
		this.bill.setBalance(100);
		this.bill.setTransactions(Set.of(ObjectFactory.getTransaction(380)));
		this.amount = 0.3 * 100 + 1;
		this.runAndAssert("You can't increase by more than 30% of balance.");
	}

}
