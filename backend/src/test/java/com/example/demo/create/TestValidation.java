package com.example.demo.create;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import java.time.LocalDate;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.ObjectFactory;
import com.example.demo.rules.BillRequest;
import com.example.demo.rules.BillResponse;
import com.example.demo.service.ExchangeRateService;
import com.example.demo.service.NksValuesService;
import com.example.demo.model.Account;
import com.example.demo.model.BillType;
import com.example.demo.utils.Constants;

@RunWith(SpringRunner.class)
public class TestValidation {

	@MockBean
	private ExchangeRateService rateService;

	@MockBean
	private NksValuesService nksValuesService;

	private KieSession kieSession;

	private Account account;
	private BillRequest request;
	private BillResponse response;
			
	@Before
	public void before() {
		KieServices kieService = KieServices.Factory.get();
		KieContainer kieContainer = kieService.newKieContainer(kieService
				.newReleaseId(Constants.KNOWLEDGE_GROUP, Constants.KNOWLEDGE_ATRIFACT, Constants.KNOWLEDGE_VERSION));
		this.kieSession = kieContainer.newKieSession(Constants.CREATE_RULES);
		this.kieSession.getAgenda().getAgendaGroup(Constants.CREATE_RULES).setFocus();
        this.kieSession.setGlobal("rateService", this.rateService);
        this.kieSession.setGlobal("nksValuesService", this.nksValuesService);        

		this.account = new Account();
		this.request = new BillRequest();
		this.response = new BillResponse();
		
		this.account.setBirthDate(LocalDate.now().plusYears(18).plusDays(1));
	}

	@After
	public void after() {
		this.kieSession.dispose();
		this.kieSession.destroy();
	}

	public void runAndAssert(String message) {
		this.kieSession.insert(this.account);
		this.kieSession.insert(this.request);
		this.kieSession.insert(this.response);
		this.kieSession.fireAllRules();
		
		assertFalse(this.response.isValid());
		assertEquals(this.response.getMessage(), message);
		assertNull(this.response.getNks());
		assertNull(this.response.getPoints());
		assertNull(this.response.getEks());
		assertNull(this.response.getReward());
	}
	
	@Test
	public void testRule1() {
		this.request.setType(BillType.RSD);
		this.request.setBase(9999);
		this.runAndAssert("Base can't be less than 10.000 RSD.");
	}

	@Test
	public void testRule2() {
		this.request.setType(BillType.RSD);
		this.request.setBase(10000001);
		this.runAndAssert("Base can't be more than 10.000.000 RSD.");
	}
	
	@Test
	public void testRule3() {
		this.request.setType(BillType.RSD);
		this.request.setBase(10001);
		this.request.setMonths(3);
		this.runAndAssert("Duration can't be less than 4 months.");
	}

	@Test
	public void testRule4() {
		this.request.setType(BillType.RSD);
		this.request.setBase(9999999);
		this.request.setMonths(43);
		this.runAndAssert("Duration can't be more than 42 months.");
	}

	@Test
	public void testRule5() {
		this.request.setType(BillType.RSD);
		this.request.setBase(5000000);
		this.request.setMonths(23);
		this.runAndAssert("Main balance must be at least 200 RSD more than base.");
	}
	
	@Test
	public void testRule6() {
		this.request.setType(BillType.RSD);
		this.request.setBase(100001);
		this.request.setMonths(4);
		this.account.setBalance(100001 + 201);
		this.account.setBirthDate(LocalDate.now());
		this.runAndAssert("Base can't be more than 100.000 RSD for underage clients.");
	}

	@Test
	public void testRule7() {
		this.request.setType(BillType.RSD);
		this.request.setBase(99999);
		this.request.setMonths(42);
		this.account.setBalance(99999 + 201);
		this.account.setBirthDate(LocalDate.now());
		this.account.setBills(Set.of(
			ObjectFactory.getBill(BillType.RSD),
			ObjectFactory.getBill(BillType.RSD)
		));
		this.runAndAssert("Underage clients can't have more than 2 active RSD bills.");
	}
	
	@Test
	public void testRule8() {
		this.request.setType(BillType.EUR);
		this.request.setBase(99);
		this.runAndAssert("Base can't be less than 100 currency units.");
	}

	@Test
	public void testRule9() {
		this.request.setType(BillType.EUR);
		this.request.setBase(100001);
		this.runAndAssert("Base can't be more than 100.000 currency units.");
	}
	
	@Test
	public void testRule10() {
		this.request.setType(BillType.EUR);
		this.request.setBase(101);
		this.request.setMonths(1);
		this.runAndAssert("Duration can't be less than 2 months.");
	}

	@Test
	public void testRule11() {
		this.request.setType(BillType.EUR);
		this.request.setBase(99999);
		this.request.setMonths(37);
		this.runAndAssert("Duration can't be more than 36 months.");
	}

	@Test
	public void testRule12() {
		this.request.setType(BillType.EUR);
		this.request.setBase(50000);
		this.request.setMonths(19);
		this.runAndAssert("Main balance must be at least 1000 RSD more than base.");
	}
	
	@Test
	public void testRule13() {
		this.request.setType(BillType.EUR);
		this.request.setBase(1001);
		this.request.setMonths(2);
		this.account.setBalance(1001 * 140 + 1001);
		this.account.setBirthDate(LocalDate.now());
		this.runAndAssert("Base can't be more than 1000 currency units for underage clients.");
	}

	@Test
	public void testRule14() {
		this.request.setType(BillType.EUR);
		this.request.setBase(999);
		this.request.setMonths(36);
		this.account.setBalance(999 * 140 + 1001);
		this.account.setBirthDate(LocalDate.now());
		this.account.setBills(Set.of(
			ObjectFactory.getBill(BillType.EUR)
		));
		this.runAndAssert("Underage clients can't have more than 1 active foreign bill.");
	}

}
