package com.example.demo.report;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.ObjectFactory;
import com.example.demo.model.Account;
import com.example.demo.model.Bill;
import com.example.demo.model.BillType;
import com.example.demo.service.ExchangeRateService;
import com.example.demo.utils.Constants;

@RunWith(SpringRunner.class)
public class TestAdvancedReports {

	@MockBean
	private ExchangeRateService rateService;

	private KieSession kieSession;
	
	private List<Account> accounts;
		
	@Before
	public void before() {
		KieServices kieService = KieServices.Factory.get();
		KieContainer kieContainer = kieService.newKieContainer(kieService
				.newReleaseId(Constants.KNOWLEDGE_GROUP, Constants.KNOWLEDGE_ATRIFACT, Constants.KNOWLEDGE_VERSION));
		this.kieSession = kieContainer.newKieSession(Constants.REPORT_RULES);
        this.kieSession.setGlobal("rateService", this.rateService);

		this.accounts = new ArrayList<>();
	}

	@After
	public void after() {
		this.kieSession.dispose();
		this.kieSession.destroy();		
	}

	@Test
	public void testRule1() {
		this.kieSession.getAgenda().getAgendaGroup(Constants.FIRST_REPORT).setFocus();

		Account account = new Account();
		account.setId(1l);
		account.setDate(LocalDate.now().minusYears(5).minusDays(1));
		account.setBills(Set.of(
			ObjectFactory.getBill(910000), ObjectFactory.getBill(910000), ObjectFactory.getBill(910000), 
			ObjectFactory.getBill(910000), ObjectFactory.getBill(910000), ObjectFactory.getBill(910000), 
			ObjectFactory.getBill(910000), ObjectFactory.getBill(910000), ObjectFactory.getBill(910000), 
			ObjectFactory.getBill(910000), ObjectFactory.getBill(910000)
		));
				
		this.accounts.addAll(List.of(account, new Account()));
		this.kieSession.insert(this.accounts);
		this.kieSession.fireAllRules();

		assertEquals(this.accounts.size(), 1);
		assertEquals(this.accounts.get(0).getId(), Long.valueOf(1));
	}

	@Test
	public void testRule2() {
		this.kieSession.getAgenda().getAgendaGroup(Constants.SECOND_REPORT).setFocus();
		Mockito.when(this.rateService.convertToRSD(BillType.EUR, 9100))
		.thenReturn(117.48 * 9100);
		
		Account account = new Account();
		account.setId(1l);
		account.setBills(Set.of(
			ObjectFactory.getBill(BillType.EUR, 9100, 9), ObjectFactory.getBill(BillType.EUR, 9100, 9), ObjectFactory.getBill(BillType.EUR, 9100, 9), 
			ObjectFactory.getBill(BillType.EUR, 9100, 9), ObjectFactory.getBill(BillType.EUR, 9100, 9), ObjectFactory.getBill(BillType.EUR, 9100, 9), 
			ObjectFactory.getBill(BillType.EUR, 9100, 9), ObjectFactory.getBill(BillType.EUR, 9100, 9), ObjectFactory.getBill(BillType.EUR, 9100, 9), 
			ObjectFactory.getBill(BillType.EUR, 9100, 9), ObjectFactory.getBill(BillType.EUR, 9100, 9)
		));
		
		this.accounts.addAll(List.of(account, new Account()));
		this.kieSession.insert(this.accounts);
		this.kieSession.fireAllRules();
		
		assertEquals(this.accounts.size(), 1);
		assertEquals(this.accounts.get(0).getId(), Long.valueOf(1));
	}
	
	@Test
	public void testRule3() {
		this.kieSession.getAgenda().getAgendaGroup(Constants.THIRD_REPORT).setFocus();
		Mockito.when(this.rateService.convertToRSD(BillType.RSD, 500000))
		.thenReturn(500000.0);
		Mockito.when(this.rateService.convertToRSD(BillType.RSD, 500001))
		.thenReturn(500001.0);

		Account account = new Account();
		account.setId(1l);
		Bill bill = ObjectFactory.getBill(BillType.RSD);
		account.setBills(Set.of(bill));
		
		bill.setTransactions(Set.of(
			ObjectFactory.getTransaction(bill, 500000), ObjectFactory.getTransaction(bill, 500000),
			ObjectFactory.getTransaction(bill, 500001)
		));
		bill.setRenewals(Set.of(
			ObjectFactory.getRenewal(6), ObjectFactory.getRenewal(6), ObjectFactory.getRenewal(6), 
			ObjectFactory.getRenewal(6), ObjectFactory.getRenewal(7)
		));
		
		this.accounts.addAll(List.of(account, new Account()));
		this.kieSession.insert(this.accounts);
		this.kieSession.fireAllRules();
		
		assertEquals(this.accounts.size(), 1);
		assertEquals(this.accounts.get(0).getId(), Long.valueOf(1));
	}

}
