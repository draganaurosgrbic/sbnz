package com.example.demo.close;

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
import com.example.demo.rules.CloseResponse;
import com.example.demo.model.Account;
import com.example.demo.model.Bill;
import com.example.demo.model.BillStatus;
import com.example.demo.model.BillType;
import com.example.demo.utils.Constants;

public class TestValidation {

	private KieSession kieSession;

	private Account account;
	private Bill bill;
	private CloseResponse response;
		
	@Before
	public void before() {
		KieServices kieService = KieServices.Factory.get();
		KieContainer kieContainer = kieService.newKieContainer(kieService
				.newReleaseId(Constants.KNOWLEDGE_GROUP, Constants.KNOWLEDGE_ATRIFACT, Constants.KNOWLEDGE_VERSION));
		this.kieSession = kieContainer.newKieSession(Constants.CLOSE_RULES);
		this.kieSession.getAgenda().getAgendaGroup(Constants.CLOSE_RULES).setFocus();

		this.account = new Account();
		this.bill = new Bill();
		this.response = new CloseResponse();

		this.account.setBills(Set.of(this.bill));
		this.bill.setAccount(this.account);
	}

	@After
	public void after() {
		this.kieSession.dispose();
		this.kieSession.destroy();
	}

	public void runAndAssert(boolean valid, String message) {
		this.kieSession.insert(this.bill);
		this.kieSession.insert(this.response);
		this.kieSession.fireAllRules();
		
		assertEquals(this.response.isValid(), valid);
		assertEquals(this.response.getMessage(), message);
	}

	@Test
	public void testRule1() {
		this.bill.setType(BillType.RSD);
		this.bill.setStartDate(LocalDate.now().minusMonths(1));
		this.bill.setEndDate(LocalDate.now());
		this.runAndAssert(false, "You can't close bill with passed time more than 80%.");
	}
	
	@Test
	public void testRule2() {
		this.bill.setType(BillType.RSD);
		this.bill.setStartDate(LocalDate.now().minusMonths(1));
		this.bill.setEndDate(LocalDate.now().plusMonths(1));
		this.bill.setBase(19999);
		this.runAndAssert(false, "You can't close RSD bill with base less than 20.000 RSD.");
	}

	@Test
	public void testRule3() {
		this.bill.setType(BillType.RSD);
		this.bill.setStartDate(LocalDate.now().minusMonths(1));
		this.bill.setEndDate(LocalDate.now().plusMonths(1));
		this.bill.setBase(20001);
		this.runAndAssert(false, "You can't close the only active RSD bill.");
	}

	@Test
	public void testRule4() {
		this.bill.setType(BillType.RSD);
		this.bill.setStartDate(LocalDate.now().minusMonths(1));
		this.bill.setEndDate(LocalDate.now().plusMonths(1));
		this.bill.setBase(20001);
		this.account.setBills(Set.of(
			ObjectFactory.getBill(BillType.RSD), ObjectFactory.getBill(BillType.RSD),
			ObjectFactory.getBill(BillStatus.ABORTED, BillType.RSD), ObjectFactory.getBill(BillStatus.ABORTED, BillType.RSD),
			ObjectFactory.getBill(BillStatus.ABORTED, BillType.RSD), ObjectFactory.getBill(BillStatus.ABORTED, BillType.RSD), 
			ObjectFactory.getBill(BillStatus.ABORTED, BillType.RSD), ObjectFactory.getBill(BillStatus.ABORTED, BillType.RSD)
		));
		this.runAndAssert(false, "You can't close more than 6 RSD bills.");
	}
	
	@Test
	public void testRule5() {
		this.bill.setType(BillType.EUR);
		this.bill.setStartDate(LocalDate.now().minusMonths(1));
		this.bill.setEndDate(LocalDate.now().plusMonths(1));
		this.bill.setBase(199);
		this.runAndAssert(false, "You can't close foreign bill with base less than 200 currency units.");
	}

	@Test
	public void testRule6() {
		this.bill.setType(BillType.EUR);
		this.bill.setStartDate(LocalDate.now().minusMonths(1));
		this.bill.setEndDate(LocalDate.now().plusMonths(1));
		this.bill.setBase(201);
		this.runAndAssert(false, "You can't close the only active foreign bill.");
	}

	@Test
	public void testRule7() {
		this.bill.setType(BillType.EUR);
		this.bill.setStartDate(LocalDate.now().minusMonths(1));
		this.bill.setEndDate(LocalDate.now().plusMonths(1));
		this.bill.setBase(201);
		this.account.setBills(Set.of(
			ObjectFactory.getBill(BillType.EUR), ObjectFactory.getBill(BillType.EUR),
			ObjectFactory.getBill(BillStatus.ABORTED, BillType.EUR), ObjectFactory.getBill(BillStatus.ABORTED, BillType.EUR),
			ObjectFactory.getBill(BillStatus.ABORTED, BillType.EUR), ObjectFactory.getBill(BillStatus.ABORTED, BillType.EUR)
		));
		this.runAndAssert(false, "You can't close more than 4 foreign bills.");
	}
	
	@Test
	public void testRule8() {
		this.bill.setType(BillType.RSD);
		this.bill.setStartDate(LocalDate.now().minusMonths(1));
		this.bill.setEndDate(LocalDate.now().plusMonths(1));
		this.bill.setBase(20001);
		this.account.setBills(Set.of(
			ObjectFactory.getBill(BillType.RSD), ObjectFactory.getBill(BillType.RSD)
		));
		this.runAndAssert(true, null);
	}
	
	@Test
	public void testRule9() {
		this.bill.setType(BillType.EUR);
		this.bill.setStartDate(LocalDate.now().minusMonths(1));
		this.bill.setEndDate(LocalDate.now().plusMonths(1));
		this.bill.setBase(201);
		this.account.setBills(Set.of(
			ObjectFactory.getBill(BillType.EUR), ObjectFactory.getBill(BillType.EUR)
		));
		this.runAndAssert(true, null);
	}

}
