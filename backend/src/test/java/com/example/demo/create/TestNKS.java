package com.example.demo.create;

import static org.junit.Assert.*;

import java.time.LocalDate;

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

import com.example.demo.rules.BillRequest;
import com.example.demo.rules.BillResponse;
import com.example.demo.service.ExchangeRateService;
import com.example.demo.service.NksValuesService;
import com.example.demo.model.Account;
import com.example.demo.model.BillType;
import com.example.demo.utils.Constants;

@RunWith(SpringRunner.class)
public class TestNKS {

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
		this.setNksValues();
	}

	@After
	public void after() {
		this.kieSession.dispose();
		this.kieSession.destroy();
	}

	public void runAndAssert(double nks) {
		this.kieSession.insert(this.account);
		this.kieSession.insert(this.request);
		this.kieSession.insert(this.response);
		this.kieSession.fireAllRules();
		
		assertTrue(this.response.isValid());
		assertNull(this.response.getMessage());
		assertEquals(this.response.getNks(), Double.valueOf(nks));
		assertNotNull(this.response.getPoints());
		assertNotNull(this.response.getEks());
		assertNotNull(this.response.getReward());
	}
	
	@Test
	public void testRule1() {
		this.request.setType(BillType.RSD);
		this.request.setBase(10001);
		this.request.setMonths(22);
		this.account.setBalance(10001 + 201);
		this.runAndAssert(1.5);
	}

	@Test
	public void testRule2() {
		this.request.setType(BillType.RSD);
		this.request.setBase(2100001);
		this.request.setMonths(21);
		this.account.setBalance(2100001 + 201);
		this.runAndAssert(1.5);
	}

	@Test
	public void testRule3() {
		this.request.setType(BillType.EUR);
		this.request.setBase(101);
		this.request.setMonths(31);
		this.account.setBalance(101 * 140 + 1001);
		this.runAndAssert(1.5);
	}

	@Test
	public void testRule4() {
		this.request.setType(BillType.EUR);
		this.request.setBase(90001);
		this.request.setMonths(30);
		this.account.setBalance(90001 * 140 + 1001);
		this.runAndAssert(1.5);
	}
	
	@Test
	public void testRule5() {
		this.request.setType(BillType.USD);
		this.request.setBase(101);
		this.request.setMonths(28);
		this.account.setBalance(101 * 140 + 1001);
		this.runAndAssert(1.5);		
	}
	
	@Test
	public void testRule6() {
		this.request.setType(BillType.USD);
		this.request.setBase(78301);
		this.request.setMonths(27);
		this.account.setBalance(78301 * 140 + 1001);
		this.runAndAssert(1.5);
	}

	@Test
	public void testRule7() {
		this.request.setType(BillType.CHF);
		this.request.setBase(101);
		this.request.setMonths(25);
		this.account.setBalance(101 * 140 + 1001);
		this.runAndAssert(1.5);		
	}
	
	@Test
	public void testRule8() {
		this.request.setType(BillType.CHF);
		this.request.setBase(67201);
		this.request.setMonths(24);
		this.account.setBalance(67201 * 140 + 1001);
		this.runAndAssert(1.5);
	}

	@Test
	public void testRule9() {
		this.request.setType(BillType.GBP);
		this.request.setBase(101);
		this.request.setMonths(34);
		this.account.setBalance(101 * 140 + 1001);
		this.runAndAssert(1.5);		
	}
	
	@Test
	public void testRule10() {
		this.request.setType(BillType.GBP);
		this.request.setBase(99331);
		this.request.setMonths(33);
		this.account.setBalance(99331 * 140 + 1001);
		this.runAndAssert(1.5);
	}

	@Test
	public void testRule11() {
		this.request.setType(BillType.RSD);
		this.request.setBase(10001);
		this.request.setMonths(16);
		this.account.setBalance(10001 + 201);
		this.runAndAssert(1.2);		
	}
	
	@Test
	public void testRule12() {
		this.request.setType(BillType.RSD);
		this.request.setBase(750001);
		this.request.setMonths(15);
		this.account.setBalance(750001 + 201);
		this.runAndAssert(1.2);
	}
	
	@Test
	public void testRule13() {
		this.request.setType(BillType.EUR);
		this.request.setBase(101);
		this.request.setMonths(25);
		this.account.setBalance(101 * 140 + 1001);
		this.runAndAssert(1.2);		
	}
	
	@Test
	public void testRule14() {
		this.request.setType(BillType.EUR);
		this.request.setBase(48001);
		this.request.setMonths(24);
		this.account.setBalance(48001 * 140 + 1001);
		this.runAndAssert(1.2);
	}

	@Test
	public void testRule15() {
		this.request.setType(BillType.USD);
		this.request.setBase(101);
		this.request.setMonths(22);
		this.account.setBalance(101 * 140 + 1001);
		this.runAndAssert(1.2);		
	}
	
	@Test
	public void testRule16() {
		this.request.setType(BillType.USD);
		this.request.setBase(39901);
		this.request.setMonths(21);
		this.account.setBalance(39901 * 140 + 1001);
		this.runAndAssert(1.2);
	}

	@Test
	public void testRule17() {
		this.request.setType(BillType.CHF);
		this.request.setBase(101);
		this.request.setMonths(19);
		this.account.setBalance(101 * 140 + 1001);
		this.runAndAssert(1.2);		
	}
	
	@Test
	public void testRule18() {
		this.request.setType(BillType.CHF);
		this.request.setBase(32401);
		this.request.setMonths(18);
		this.account.setBalance(32401 * 140 + 1001);
		this.runAndAssert(1.2);
	}

	@Test
	public void testRule19() {
		this.request.setType(BillType.GBP);
		this.request.setBase(101);
		this.request.setMonths(28);
		this.account.setBalance(101 * 140 + 1001);
		this.runAndAssert(1.2);		
	}
	
	@Test
	public void testRule20() {
		this.request.setType(BillType.GBP);
		this.request.setBase(56701);
		this.request.setMonths(27);
		this.account.setBalance(56701 * 140 + 1001);
		this.runAndAssert(1.2);
	}
	
	@Test
	public void testRule21() {
		this.request.setType(BillType.RSD);
		this.request.setBase(10001);
		this.request.setMonths(10);
		this.account.setBalance(10001 + 201);
		this.runAndAssert(1.0);		
	}
	
	@Test
	public void testRule22() {
		this.request.setType(BillType.RSD);
		this.request.setBase(270001);
		this.request.setMonths(9);
		this.account.setBalance(270001 + 201);
		this.runAndAssert(1.0);
	}

	@Test
	public void testRule23() {
		this.request.setType(BillType.EUR);
		this.request.setBase(101);
		this.request.setMonths(19);
		this.account.setBalance(101 * 140 + 1001);
		this.runAndAssert(1.0);		
	}
	
	@Test
	public void testRule24() {
		this.request.setType(BillType.EUR);
		this.request.setBase(18001);
		this.request.setMonths(18);
		this.account.setBalance(18001 * 140 + 1001);
		this.runAndAssert(1.0);
	}

	@Test
	public void testRule25() {
		this.request.setType(BillType.USD);
		this.request.setBase(101);
		this.request.setMonths(16);
		this.account.setBalance(101 * 140 + 1001);
		this.runAndAssert(1.0);		
	}
	
	@Test
	public void testRule26() {
		this.request.setType(BillType.USD);
		this.request.setBase(13501);
		this.request.setMonths(15);
		this.account.setBalance(13501 * 140 + 1001);
		this.runAndAssert(1.0);
	}
	
	@Test
	public void testRule27() {
		this.request.setType(BillType.CHF);
		this.request.setBase(101);
		this.request.setMonths(13);
		this.account.setBalance(101 * 140 + 1001);
		this.runAndAssert(1.0);		
	}
	
	@Test
	public void testRule28() {
		this.request.setType(BillType.CHF);
		this.request.setBase(9601);
		this.request.setMonths(12);
		this.account.setBalance(9601 * 140 + 1001);
		this.runAndAssert(1.0);
	}

	@Test
	public void testRule29() {
		this.request.setType(BillType.GBP);
		this.request.setBase(101);
		this.request.setMonths(22);
		this.account.setBalance(101 * 140 + 1001);
		this.runAndAssert(1.0);		
	}
	
	@Test
	public void testRule30() {
		this.request.setType(BillType.GBP);
		this.request.setBase(23101);
		this.request.setMonths(21);
		this.account.setBalance(23101 * 140 + 1001);
		this.runAndAssert(1.0);
	}

	@Test
	public void testRule31() {
		this.request.setType(BillType.RSD);
		this.request.setBase(10001);
		this.request.setMonths(7);
		this.account.setBalance(10001 + 201);
		this.runAndAssert(0.8);		
	}
	
	@Test
	public void testRule32() {
		this.request.setType(BillType.RSD);
		this.request.setBase(60001);
		this.request.setMonths(6);
		this.account.setBalance(60001 + 201);
		this.runAndAssert(0.8);
	}

	@Test
	public void testRule33() {
		this.request.setType(BillType.EUR);
		this.request.setBase(101);
		this.request.setMonths(13);
		this.account.setBalance(101 * 140 + 1001);
		this.runAndAssert(0.8);		
	}
	
	@Test
	public void testRule34() {
		this.request.setType(BillType.EUR);
		this.request.setBase(6001);
		this.request.setMonths(12);
		this.account.setBalance(6001 * 140 + 1001);
		this.runAndAssert(0.8);
	}
	
	@Test
	public void testRule35() {
		this.request.setType(BillType.USD);
		this.request.setBase(101);
		this.request.setMonths(10);
		this.account.setBalance(101 * 140 + 1001);
		this.runAndAssert(0.8);		
	}
	
	@Test
	public void testRule36() {
		this.request.setType(BillType.USD);
		this.request.setBase(4321);
		this.request.setMonths(9);
		this.account.setBalance(4321 * 140 + 1001);
		this.runAndAssert(0.8);
	}

	@Test
	public void testRule37() {
		this.request.setType(BillType.CHF);
		this.request.setBase(101);
		this.request.setMonths(7);
		this.account.setBalance(101 * 140 + 1001);
		this.runAndAssert(0.8);		
	}
	
	@Test
	public void testRule38() {
		this.request.setType(BillType.CHF);
		this.request.setBase(2821);
		this.request.setMonths(6);
		this.account.setBalance(2821 * 140 + 1001);
		this.runAndAssert(0.8);
	}

	@Test
	public void testRule39() {
		this.request.setType(BillType.GBP);
		this.request.setBase(101);
		this.request.setMonths(16);
		this.account.setBalance(101 * 140 + 1001);
		this.runAndAssert(0.8);		
	}
	
	@Test
	public void testRule40() {
		this.request.setType(BillType.GBP);
		this.request.setBase(7801);
		this.request.setMonths(15);
		this.account.setBalance(7801 * 140 + 1001);
		this.runAndAssert(0.8);
	}

	@Test
	public void testRule41() {
		this.request.setType(BillType.RSD);
		this.request.setBase(10001);
		this.request.setMonths(6);
		this.account.setBalance(10001 + 201);
		this.runAndAssert(0.5);		
	}
	
	@Test
	public void testRule42() {
		this.request.setType(BillType.RSD);
		this.request.setBase(60000);
		this.request.setMonths(6);
		this.account.setBalance(60000 + 201);
		this.runAndAssert(0.5);
	}

	@Test
	public void testRule43() {
		this.request.setType(BillType.EUR);
		this.request.setBase(101);
		this.request.setMonths(12);
		this.account.setBalance(101 * 140 + 1001);
		this.runAndAssert(0.5);		
	}
	
	@Test
	public void testRule44() {
		this.request.setType(BillType.EUR);
		this.request.setBase(6000);
		this.request.setMonths(12);
		this.account.setBalance(6000 * 140 + 1001);
		this.runAndAssert(0.5);
	}

	@Test
	public void testRule45() {
		this.request.setType(BillType.USD);
		this.request.setBase(101);
		this.request.setMonths(9);
		this.account.setBalance(101 * 140 + 1001);
		this.runAndAssert(0.5);		
	}
	
	@Test
	public void testRule46() {
		this.request.setType(BillType.USD);
		this.request.setBase(4320);
		this.request.setMonths(9);
		this.account.setBalance(4320 * 140 + 1001);
		this.runAndAssert(0.5);
	}
	
	@Test
	public void testRule47() {
		this.request.setType(BillType.CHF);
		this.request.setBase(101);
		this.request.setMonths(6);
		this.account.setBalance(101 * 140 + 1001);
		this.runAndAssert(0.5);		
	}
	
	@Test
	public void testRule48() {
		this.request.setType(BillType.CHF);
		this.request.setBase(2820);
		this.request.setMonths(6);
		this.account.setBalance(2820 * 140 + 1001);
		this.runAndAssert(0.5);
	}

	@Test
	public void testRule49() {
		this.request.setType(BillType.GBP);
		this.request.setBase(101);
		this.request.setMonths(15);
		this.account.setBalance(101 * 140 + 1001);
		this.runAndAssert(0.5);		
	}
	
	@Test
	public void testRule50() {
		this.request.setType(BillType.GBP);
		this.request.setBase(7800);
		this.request.setMonths(15);
		this.account.setBalance(7800 * 140 + 1001);
		this.runAndAssert(0.5);
	}
	
	private void setNksValues() {
        Mockito.when(this.nksValuesService.monthsRate(1, BillType.RSD)).thenReturn(21.0);
        Mockito.when(this.nksValuesService.baseRate(1, BillType.RSD)).thenReturn(100000.0);
        Mockito.when(this.nksValuesService.monthsRate(1, BillType.EUR)).thenReturn(30.0);
        Mockito.when(this.nksValuesService.baseRate(1, BillType.EUR)).thenReturn(3000.0);
        Mockito.when(this.nksValuesService.monthsRate(1, BillType.USD)).thenReturn(27.0);
        Mockito.when(this.nksValuesService.baseRate(1, BillType.USD)).thenReturn(2900.0);
        Mockito.when(this.nksValuesService.monthsRate(1, BillType.CHF)).thenReturn(24.0);
        Mockito.when(this.nksValuesService.baseRate(1, BillType.CHF)).thenReturn(2800.0);
        Mockito.when(this.nksValuesService.monthsRate(1, BillType.GBP)).thenReturn(33.0);
        Mockito.when(this.nksValuesService.baseRate(1, BillType.GBP)).thenReturn(3010.0);

        Mockito.when(this.nksValuesService.monthsRate(2, BillType.RSD)).thenReturn(15.0);
        Mockito.when(this.nksValuesService.baseRate(2, BillType.RSD)).thenReturn(50000.0);
        Mockito.when(this.nksValuesService.monthsRate(2, BillType.EUR)).thenReturn(24.0);
        Mockito.when(this.nksValuesService.baseRate(2, BillType.EUR)).thenReturn(2000.0);
        Mockito.when(this.nksValuesService.monthsRate(2, BillType.USD)).thenReturn(21.0);
        Mockito.when(this.nksValuesService.baseRate(2, BillType.USD)).thenReturn(1900.0);
        Mockito.when(this.nksValuesService.monthsRate(2, BillType.CHF)).thenReturn(18.0);
        Mockito.when(this.nksValuesService.baseRate(2, BillType.CHF)).thenReturn(1800.0);
        Mockito.when(this.nksValuesService.monthsRate(2, BillType.GBP)).thenReturn(27.0);
        Mockito.when(this.nksValuesService.baseRate(2, BillType.GBP)).thenReturn(2100.0);

        Mockito.when(this.nksValuesService.monthsRate(3, BillType.RSD)).thenReturn(9.0);
        Mockito.when(this.nksValuesService.baseRate(3, BillType.RSD)).thenReturn(30000.0);
        Mockito.when(this.nksValuesService.monthsRate(3, BillType.EUR)).thenReturn(18.0);
        Mockito.when(this.nksValuesService.baseRate(3, BillType.EUR)).thenReturn(1000.0);
        Mockito.when(this.nksValuesService.monthsRate(3, BillType.USD)).thenReturn(15.0);
        Mockito.when(this.nksValuesService.baseRate(3, BillType.USD)).thenReturn(900.0);
        Mockito.when(this.nksValuesService.monthsRate(3, BillType.CHF)).thenReturn(12.0);
        Mockito.when(this.nksValuesService.baseRate(3, BillType.CHF)).thenReturn(800.0);
        Mockito.when(this.nksValuesService.monthsRate(3, BillType.GBP)).thenReturn(21.0);
        Mockito.when(this.nksValuesService.baseRate(3, BillType.GBP)).thenReturn(1100.0);

        Mockito.when(this.nksValuesService.monthsRate(4, BillType.RSD)).thenReturn(6.0);
        Mockito.when(this.nksValuesService.baseRate(4, BillType.RSD)).thenReturn(10000.0);
        Mockito.when(this.nksValuesService.monthsRate(4, BillType.EUR)).thenReturn(12.0);
        Mockito.when(this.nksValuesService.baseRate(4, BillType.EUR)).thenReturn(500.0);
        Mockito.when(this.nksValuesService.monthsRate(4, BillType.USD)).thenReturn(9.0);
        Mockito.when(this.nksValuesService.baseRate(4, BillType.USD)).thenReturn(480.0);
        Mockito.when(this.nksValuesService.monthsRate(4, BillType.CHF)).thenReturn(6.0);
        Mockito.when(this.nksValuesService.baseRate(4, BillType.CHF)).thenReturn(470.0);
        Mockito.when(this.nksValuesService.monthsRate(4, BillType.GBP)).thenReturn(15.0);
        Mockito.when(this.nksValuesService.baseRate(4, BillType.GBP)).thenReturn(520.0);
	}

}
