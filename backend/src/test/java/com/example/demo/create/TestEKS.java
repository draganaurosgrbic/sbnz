package com.example.demo.create;

import static org.junit.Assert.*;

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
import com.example.demo.model.BillStatus;
import com.example.demo.model.BillType;
import com.example.demo.utils.Constants;

@RunWith(SpringRunner.class)
public class TestEKS {

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
		
		this.request.setType(BillType.RSD);
		this.request.setBase(10001);
		this.request.setMonths(4);
		this.account.setBalance(10001 * 140 + 1001);
		this.account.setBirthDate(LocalDate.now().plusYears(18).plusDays(1));
	}

	@After
	public void after() {
		this.kieSession.dispose();
		this.kieSession.destroy();
	}

	public void runAndAssert(double eks, int reward) {
		this.kieSession.insert(this.account);
		this.kieSession.insert(this.request);
		this.kieSession.insert(this.response);
		this.kieSession.fireAllRules();
		
		assertTrue(this.response.isValid());
		assertNull(this.response.getMessage());
		assertNotNull(this.response.getNks());
		assertNotNull(this.response.getPoints());
		assertEquals(this.response.getEks(), Double.valueOf(eks));
		assertEquals(this.response.getReward(), Integer.valueOf(reward));
	}
	
	@Test
	public void testRule1() {
		this.account.setDate(LocalDate.now().plusYears(5).plusDays(1));
		this.account.setBills(Set.of(
			ObjectFactory.getBill(BillType.RSD, 100001, LocalDate.now().minusMonths(9).plusDays(1), LocalDate.now().minusMonths(9).plusDays(2))		
		));
		this.runAndAssert(1.0, 5000);
	}
	
	@Test
	public void testRule2() {
		this.account.setDate(LocalDate.now().plusYears(0).plusDays(1));
		this.runAndAssert(1.0, 5000);		
	}

	@Test
	public void testRule3() {
		this.request.setType(BillType.EUR);
		this.account.setDate(LocalDate.now().plusYears(0).plusDays(1));
		this.runAndAssert(1.0, 5000);
	}
	
	@Test
	public void testRule4() {
		this.account.setDate(LocalDate.now().plusYears(4).plusDays(1));
		this.account.setBills(Set.of(
			ObjectFactory.getBill(BillType.RSD, 300001, LocalDate.now().minusMonths(9).plusDays(1), LocalDate.now().minusMonths(9).plusDays(2))		
		));
		this.runAndAssert(0.98, 2000);		
	}
	
	@Test
	public void testRule5() {
		this.request.setType(BillType.EUR);
		this.account.setDate(LocalDate.now().plusYears(0).plusDays(1));
		this.account.setBills(Set.of(
			ObjectFactory.getBill(BillType.GBP, 0, LocalDate.now().minusMonths(9).plusDays(1), LocalDate.now().minusMonths(9).plusDays(2))
		));
		this.runAndAssert(0.98, 2000);
	}
	
	@Test
	public void testRule6() {
		this.request.setType(BillType.USD);
		this.account.setDate(LocalDate.now().plusYears(0).plusDays(1));
		this.account.setBills(Set.of(
			ObjectFactory.getBill(BillType.GBP, 0, LocalDate.now().minusMonths(9).plusDays(1), LocalDate.now().minusMonths(9).plusDays(2))		
		));
		this.runAndAssert(0.98, 2000);
	}
	
	@Test
	public void testRule7() {
		this.account.setDate(LocalDate.now().plusYears(3).plusDays(1));
		this.account.setBills(Set.of(
			ObjectFactory.getBill(BillType.RSD, 100, LocalDate.now().minusMonths(9).plusDays(1), LocalDate.now().minusMonths(9).plusDays(2), Set.of(ObjectFactory.getTransaction(0.3 * 100 + 1))),
			ObjectFactory.getBill(BillType.RSD, 100, LocalDate.now().minusMonths(9).plusDays(1), LocalDate.now().minusMonths(9).plusDays(2), Set.of(ObjectFactory.getTransaction(0.3 * 100 + 1)))
		));
		this.runAndAssert(0.95, 1000);		
	}
	
	@Test
	public void testRule8() {
		this.request.setType(BillType.CHF);
		this.account.setDate(LocalDate.now().plusYears(0).plusDays(1));
		this.account.setBills(Set.of(
			ObjectFactory.getBill(BillType.EUR, 0, LocalDate.now().minusMonths(9).plusDays(1), LocalDate.now().minusMonths(9).plusDays(2))		
		));
		this.runAndAssert(0.95, 1000);		
	}
	
	@Test
	public void testRule9() {
		this.request.setType(BillType.GBP);
		this.account.setDate(LocalDate.now().plusYears(0).plusDays(1));
		this.account.setBills(Set.of(
			ObjectFactory.getBill(BillType.EUR, 0, LocalDate.now().minusMonths(9).plusDays(1), LocalDate.now().minusMonths(9).plusDays(2))		
		));
		this.runAndAssert(0.95, 1000);
	}
	
	@Test
	public void testRule10() {
		this.account.setDate(LocalDate.now().plusYears(2).plusDays(1));
		this.account.setBills(Set.of(
			ObjectFactory.getBill(BillType.RSD, 0, LocalDate.now().minusMonths(9).plusDays(1), LocalDate.now().minusMonths(9).plusDays(2), Set.of(ObjectFactory.getTransaction(50001), ObjectFactory.getTransaction(50001))),
			ObjectFactory.getBill(BillType.RSD, 0, LocalDate.now().minusMonths(9).plusDays(1), LocalDate.now().minusMonths(9).plusDays(2), Set.of(ObjectFactory.getTransaction(50001), ObjectFactory.getTransaction(50001)))
		));
		this.runAndAssert(0.9, 500);		
	}
	
	@Test
	public void testRule11() {
		this.account.setDate(LocalDate.now().plusYears(0).plusDays(1));
		this.account.setBills(Set.of(
			ObjectFactory.getBill(BillStatus.CLOSED, BillType.RSD, 0, LocalDate.now().minusMonths(9).plusDays(1), LocalDate.now().minusMonths(9).plusDays(2))
		));
		this.runAndAssert(0.9, 500);
	}
	
	@Test
	public void testRule12() {
		this.account.setDate(LocalDate.now().plusYears(1).plusDays(1));
		this.account.setBills(Set.of(
			ObjectFactory.getBill(BillType.RSD, 0, LocalDate.now().minusMonths(9).plusDays(1), LocalDate.now().minusMonths(3).plusDays(2), Set.of(ObjectFactory.getTransaction(10001))),
			ObjectFactory.getBill(BillType.RSD, 0, LocalDate.now().minusMonths(9).plusDays(1), LocalDate.now().minusMonths(3).plusDays(2), Set.of(ObjectFactory.getTransaction(10001)))
		));
		this.runAndAssert(0.87, 200);
	}
	
	@Test
	public void testRule13() {
		this.request.setType(BillType.EUR);
		this.account.setDate(LocalDate.now().plusYears(0).plusDays(1));
		this.account.setBills(Set.of(
			ObjectFactory.getBill(BillStatus.CLOSED, BillType.EUR, 0, LocalDate.now().minusMonths(9).plusDays(1), LocalDate.now().minusMonths(9).plusDays(2))
		));
		this.runAndAssert(0.87, 200);
	}
	
	@Test
	public void testRule14() {
		this.account.setDate(LocalDate.now().plusYears(0).plusDays(1));
		this.account.setBills(Set.of(
			ObjectFactory.getBill(BillType.RSD, 0, LocalDate.now().minusMonths(9).plusDays(1), LocalDate.now().minusMonths(9).plusDays(2))
		));
		this.runAndAssert(0.85, 0);
	}

}
