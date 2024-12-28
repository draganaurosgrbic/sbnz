package com.example.demo.events;

import java.util.concurrent.TimeUnit;

import org.drools.core.ClassObjectFilter;
import org.drools.core.time.SessionPseudoClock;
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

import com.example.demo.model.Account;
import com.example.demo.model.Bill;
import com.example.demo.model.BillType;
import com.example.demo.model.Notification;
import com.example.demo.model.Renewal;
import com.example.demo.model.Transaction;
import com.example.demo.model.User;
import com.example.demo.service.NotificationService;
import com.example.demo.utils.Constants;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

@RunWith(SpringRunner.class)
public class TestEvents {

	@MockBean
	private NotificationService notificationService;
	
	private KieSession kieSession;
	private SessionPseudoClock clock;
	
	private Account account;
	private Bill bill;

	@Before
	public void before() {
		KieServices kieService = KieServices.Factory.get();
		KieContainer kieContainer = kieService.newKieContainer(kieService
				.newReleaseId(Constants.KNOWLEDGE_GROUP, Constants.KNOWLEDGE_ATRIFACT, Constants.KNOWLEDGE_VERSION));
		this.kieSession = kieContainer.newKieSession(Constants.EVENT_RULES_PSEUDO);
        this.kieSession.setGlobal("notificationService", this.notificationService);
        this.kieSession.setGlobal("account", this.account);
        
        this.clock = this.kieSession.getSessionClock();
        this.account = new Account();
        this.bill = new Bill();
        
        this.account.setUser(new User());
	}

	@After
	public void after() {
		this.kieSession.dispose();
		this.kieSession.destroy();
	}

	@Test
	public void testRule1() {
		this.kieSession.insert(new CreateBillEvent(this.bill));
		this.clock.advanceTime(100, TimeUnit.MILLISECONDS);
		this.kieSession.insert(new CloseBillEvent(this.bill));
		this.clock.advanceTime(100, TimeUnit.MILLISECONDS);
		this.kieSession.fireAllRules();
		
		Mockito.verify(this.notificationService, times(1)).save(any(Notification.class));
		assertEquals(this.kieSession.getObjects(new ClassObjectFilter(CreateBillEvent.class)).size(), 1);
		assertEquals(this.kieSession.getObjects(new ClassObjectFilter(CloseBillEvent.class)).size(), 1);
		assertEquals(this.kieSession.getObjects(new ClassObjectFilter(SuspiciousClientEvent.class)).size(), 1);
        
		this.clock.advanceTime(36, TimeUnit.HOURS);
		this.kieSession.fireAllRules();
		assertEquals(this.kieSession.getObjects(new ClassObjectFilter(CreateBillEvent.class)).size(), 0);
		assertEquals(this.kieSession.getObjects(new ClassObjectFilter(CloseBillEvent.class)).size(), 0);
		assertEquals(this.kieSession.getObjects(new ClassObjectFilter(SuspiciousClientEvent.class)).size(), 1);

		this.clock.advanceTime(84, TimeUnit.HOURS);
		this.kieSession.fireAllRules();
		assertEquals(this.kieSession.getObjects(new ClassObjectFilter(CreateBillEvent.class)).size(), 0);
		assertEquals(this.kieSession.getObjects(new ClassObjectFilter(CloseBillEvent.class)).size(), 0);
		assertEquals(this.kieSession.getObjects(new ClassObjectFilter(SuspiciousClientEvent.class)).size(), 0);
	}
	
	@Test
	public void testRule2() {
		this.kieSession.insert(new IncreaseBillEvent(new Transaction(this.bill, 0)));
		this.clock.advanceTime(100, TimeUnit.MILLISECONDS);
		this.kieSession.insert(new CloseBillEvent(this.bill));
		this.clock.advanceTime(100, TimeUnit.MILLISECONDS);
		this.kieSession.fireAllRules();
		
		Mockito.verify(this.notificationService, times(1)).save(any(Notification.class));
		assertEquals(this.kieSession.getObjects(new ClassObjectFilter(IncreaseBillEvent.class)).size(), 1);
		assertEquals(this.kieSession.getObjects(new ClassObjectFilter(CloseBillEvent.class)).size(), 1);
		assertEquals(this.kieSession.getObjects(new ClassObjectFilter(SuspiciousClientEvent.class)).size(), 1);
        
		this.clock.advanceTime(36, TimeUnit.HOURS);
		this.kieSession.fireAllRules();
		assertEquals(this.kieSession.getObjects(new ClassObjectFilter(IncreaseBillEvent.class)).size(), 0);
		assertEquals(this.kieSession.getObjects(new ClassObjectFilter(CloseBillEvent.class)).size(), 0);
		assertEquals(this.kieSession.getObjects(new ClassObjectFilter(SuspiciousClientEvent.class)).size(), 1);

		this.clock.advanceTime(84, TimeUnit.HOURS);
		this.kieSession.fireAllRules();
		assertEquals(this.kieSession.getObjects(new ClassObjectFilter(IncreaseBillEvent.class)).size(), 0);
		assertEquals(this.kieSession.getObjects(new ClassObjectFilter(CloseBillEvent.class)).size(), 0);
		assertEquals(this.kieSession.getObjects(new ClassObjectFilter(SuspiciousClientEvent.class)).size(), 0);
	}
	
	@Test
	public void testRule3() {
		this.kieSession.insert(new RenewBillEvent(new Renewal(this.bill, 0)));
		this.clock.advanceTime(100, TimeUnit.MILLISECONDS);
		this.kieSession.insert(new CloseBillEvent(this.bill));
		this.clock.advanceTime(100, TimeUnit.MILLISECONDS);
		this.kieSession.fireAllRules();
		
		Mockito.verify(this.notificationService, times(1)).save(any(Notification.class));
		assertEquals(this.kieSession.getObjects(new ClassObjectFilter(RenewBillEvent.class)).size(), 1);
		assertEquals(this.kieSession.getObjects(new ClassObjectFilter(CloseBillEvent.class)).size(), 1);
		assertEquals(this.kieSession.getObjects(new ClassObjectFilter(SuspiciousClientEvent.class)).size(), 1);
        
		this.clock.advanceTime(36, TimeUnit.HOURS);
		this.kieSession.fireAllRules();
		assertEquals(this.kieSession.getObjects(new ClassObjectFilter(RenewBillEvent.class)).size(), 0);
		assertEquals(this.kieSession.getObjects(new ClassObjectFilter(CloseBillEvent.class)).size(), 0);
		assertEquals(this.kieSession.getObjects(new ClassObjectFilter(SuspiciousClientEvent.class)).size(), 1);

		this.clock.advanceTime(84, TimeUnit.HOURS);
		this.kieSession.fireAllRules();
		assertEquals(this.kieSession.getObjects(new ClassObjectFilter(RenewBillEvent.class)).size(), 0);
		assertEquals(this.kieSession.getObjects(new ClassObjectFilter(CloseBillEvent.class)).size(), 0);
		assertEquals(this.kieSession.getObjects(new ClassObjectFilter(SuspiciousClientEvent.class)).size(), 0);
	}
	
	@Test
	public void testRule4() {
        this.bill.setType(BillType.RSD);
        this.bill.setBase(25001);
		this.kieSession.insert(new CloseBillEvent(this.bill));
		this.clock.advanceTime(100, TimeUnit.MILLISECONDS);
		this.kieSession.insert(new CloseBillEvent(this.bill));
		this.clock.advanceTime(100, TimeUnit.MILLISECONDS);
		this.kieSession.insert(new CloseBillEvent(this.bill));
		this.clock.advanceTime(100, TimeUnit.MILLISECONDS);
		this.kieSession.insert(new CloseBillEvent(this.bill));
		this.clock.advanceTime(100, TimeUnit.MILLISECONDS);
		this.kieSession.fireAllRules();
		
		Mockito.verify(this.notificationService, times(1)).save(any(Notification.class));
		assertEquals(this.kieSession.getObjects(new ClassObjectFilter(CloseBillEvent.class)).size(), 4);
		assertEquals(this.kieSession.getObjects(new ClassObjectFilter(SuspiciousClientEvent.class)).size(), 1);

		this.clock.advanceTime(36, TimeUnit.HOURS);
		this.kieSession.fireAllRules();
		assertEquals(this.kieSession.getObjects(new ClassObjectFilter(CloseBillEvent.class)).size(), 0);
		assertEquals(this.kieSession.getObjects(new ClassObjectFilter(SuspiciousClientEvent.class)).size(), 1);

		this.clock.advanceTime(84, TimeUnit.HOURS);
		this.kieSession.fireAllRules();
		assertEquals(this.kieSession.getObjects(new ClassObjectFilter(CloseBillEvent.class)).size(), 0);
		assertEquals(this.kieSession.getObjects(new ClassObjectFilter(SuspiciousClientEvent.class)).size(), 0);
	}
	
	@Test
	public void testRule5() {
		this.bill.setType(BillType.EUR);
        this.bill.setBase(251);
		this.kieSession.insert(new CloseBillEvent(this.bill));
		this.clock.advanceTime(100, TimeUnit.MILLISECONDS);
		this.kieSession.insert(new CloseBillEvent(this.bill));
		this.clock.advanceTime(100, TimeUnit.MILLISECONDS);
		this.kieSession.insert(new CloseBillEvent(this.bill));
		this.clock.advanceTime(100, TimeUnit.MILLISECONDS);
		this.kieSession.fireAllRules();
		
		Mockito.verify(this.notificationService, times(1)).save(any(Notification.class));
		assertEquals(this.kieSession.getObjects(new ClassObjectFilter(CloseBillEvent.class)).size(), 3);
		assertEquals(this.kieSession.getObjects(new ClassObjectFilter(SuspiciousClientEvent.class)).size(), 1);
        
		this.clock.advanceTime(36, TimeUnit.HOURS);
		this.kieSession.fireAllRules();
		assertEquals(this.kieSession.getObjects(new ClassObjectFilter(CloseBillEvent.class)).size(), 0);
		assertEquals(this.kieSession.getObjects(new ClassObjectFilter(SuspiciousClientEvent.class)).size(), 1);

		this.clock.advanceTime(84, TimeUnit.HOURS);
		this.kieSession.fireAllRules();
		assertEquals(this.kieSession.getObjects(new ClassObjectFilter(CloseBillEvent.class)).size(), 0);
		assertEquals(this.kieSession.getObjects(new ClassObjectFilter(SuspiciousClientEvent.class)).size(), 0);
	}

	@Test
	public void testRule6() {
        this.bill.setType(BillType.RSD);
		this.kieSession.insert(new IncreaseBillEvent(new Transaction(this.bill, 30001)));
		this.clock.advanceTime(100, TimeUnit.MILLISECONDS);
		this.kieSession.insert(new IncreaseBillEvent(new Transaction(this.bill, 30001)));
		this.clock.advanceTime(100, TimeUnit.MILLISECONDS);
		this.kieSession.insert(new IncreaseBillEvent(new Transaction(this.bill, 30001)));
		this.clock.advanceTime(100, TimeUnit.MILLISECONDS);
		this.kieSession.insert(new IncreaseBillEvent(new Transaction(this.bill, 30001)));
		this.clock.advanceTime(100, TimeUnit.MILLISECONDS);
		this.kieSession.fireAllRules();
		
		Mockito.verify(this.notificationService, times(1)).save(any(Notification.class));
		assertEquals(this.kieSession.getObjects(new ClassObjectFilter(IncreaseBillEvent.class)).size(), 4);
		assertEquals(this.kieSession.getObjects(new ClassObjectFilter(SuspiciousClientEvent.class)).size(), 1);
        
		this.clock.advanceTime(36, TimeUnit.HOURS);
		this.kieSession.fireAllRules();
		assertEquals(this.kieSession.getObjects(new ClassObjectFilter(IncreaseBillEvent.class)).size(), 0);
		assertEquals(this.kieSession.getObjects(new ClassObjectFilter(SuspiciousClientEvent.class)).size(), 1);

		this.clock.advanceTime(84, TimeUnit.HOURS);
		this.kieSession.fireAllRules();
		assertEquals(this.kieSession.getObjects(new ClassObjectFilter(IncreaseBillEvent.class)).size(), 0);
		assertEquals(this.kieSession.getObjects(new ClassObjectFilter(SuspiciousClientEvent.class)).size(), 0);
	}

	@Test
	public void testRule7() {
		this.bill.setType(BillType.EUR);
		this.kieSession.insert(new IncreaseBillEvent(new Transaction(this.bill, 301)));
		this.clock.advanceTime(100, TimeUnit.MILLISECONDS);
		this.kieSession.insert(new IncreaseBillEvent(new Transaction(this.bill, 301)));
		this.clock.advanceTime(100, TimeUnit.MILLISECONDS);
		this.kieSession.insert(new IncreaseBillEvent(new Transaction(this.bill, 301)));
		this.clock.advanceTime(100, TimeUnit.MILLISECONDS);
		this.kieSession.fireAllRules();
		
		Mockito.verify(this.notificationService, times(1)).save(any(Notification.class));
		assertEquals(this.kieSession.getObjects(new ClassObjectFilter(IncreaseBillEvent.class)).size(), 3);
		assertEquals(this.kieSession.getObjects(new ClassObjectFilter(SuspiciousClientEvent.class)).size(), 1);
        
		this.clock.advanceTime(36, TimeUnit.HOURS);
		this.kieSession.fireAllRules();
		assertEquals(this.kieSession.getObjects(new ClassObjectFilter(IncreaseBillEvent.class)).size(), 0);
		assertEquals(this.kieSession.getObjects(new ClassObjectFilter(SuspiciousClientEvent.class)).size(), 1);

		this.clock.advanceTime(84, TimeUnit.HOURS);
		this.kieSession.fireAllRules();
		assertEquals(this.kieSession.getObjects(new ClassObjectFilter(IncreaseBillEvent.class)).size(), 0);
		assertEquals(this.kieSession.getObjects(new ClassObjectFilter(SuspiciousClientEvent.class)).size(), 0);
	}

	@Test
	public void testRule8() {
        this.bill.setType(BillType.RSD);
		this.kieSession.insert(new RenewBillEvent(new Renewal(this.bill, 10)));
		this.clock.advanceTime(100, TimeUnit.MILLISECONDS);
		this.kieSession.insert(new RenewBillEvent(new Renewal(this.bill, 10)));
		this.clock.advanceTime(100, TimeUnit.MILLISECONDS);
		this.kieSession.insert(new RenewBillEvent(new Renewal(this.bill, 10)));
		this.clock.advanceTime(100, TimeUnit.MILLISECONDS);
		this.kieSession.insert(new RenewBillEvent(new Renewal(this.bill, 10)));
		this.clock.advanceTime(100, TimeUnit.MILLISECONDS);
		this.kieSession.fireAllRules();
		
		Mockito.verify(this.notificationService, times(1)).save(any(Notification.class));
		assertEquals(this.kieSession.getObjects(new ClassObjectFilter(RenewBillEvent.class)).size(), 4);
		assertEquals(this.kieSession.getObjects(new ClassObjectFilter(SuspiciousClientEvent.class)).size(), 1);
        
		this.clock.advanceTime(36, TimeUnit.HOURS);
		this.kieSession.fireAllRules();
		assertEquals(this.kieSession.getObjects(new ClassObjectFilter(RenewBillEvent.class)).size(), 0);
		assertEquals(this.kieSession.getObjects(new ClassObjectFilter(SuspiciousClientEvent.class)).size(), 1);

		this.clock.advanceTime(84, TimeUnit.HOURS);
		this.kieSession.fireAllRules();
		assertEquals(this.kieSession.getObjects(new ClassObjectFilter(RenewBillEvent.class)).size(), 0);
		assertEquals(this.kieSession.getObjects(new ClassObjectFilter(SuspiciousClientEvent.class)).size(), 0);
	}
	
	@Test
	public void testRule9() {
		this.bill.setType(BillType.EUR);
		this.kieSession.insert(new RenewBillEvent(new Renewal(this.bill, 7)));
		this.clock.advanceTime(100, TimeUnit.MILLISECONDS);
		this.kieSession.insert(new RenewBillEvent(new Renewal(this.bill, 7)));
		this.clock.advanceTime(100, TimeUnit.MILLISECONDS);
		this.kieSession.insert(new RenewBillEvent(new Renewal(this.bill, 7)));
		this.clock.advanceTime(100, TimeUnit.MILLISECONDS);
		this.kieSession.fireAllRules();

		Mockito.verify(this.notificationService, times(1)).save(any(Notification.class));
		assertEquals(this.kieSession.getObjects(new ClassObjectFilter(RenewBillEvent.class)).size(), 3);
		assertEquals(this.kieSession.getObjects(new ClassObjectFilter(SuspiciousClientEvent.class)).size(), 1);
        
		this.clock.advanceTime(35, TimeUnit.HOURS);
		this.kieSession.fireAllRules();
		assertEquals(this.kieSession.getObjects(new ClassObjectFilter(RenewBillEvent.class)).size(), 3);
		assertEquals(this.kieSession.getObjects(new ClassObjectFilter(SuspiciousClientEvent.class)).size(), 1);
		
		this.clock.advanceTime(36, TimeUnit.HOURS);
		this.kieSession.fireAllRules();
		assertEquals(this.kieSession.getObjects(new ClassObjectFilter(RenewBillEvent.class)).size(), 0);
		assertEquals(this.kieSession.getObjects(new ClassObjectFilter(SuspiciousClientEvent.class)).size(), 1);

		this.clock.advanceTime(84, TimeUnit.HOURS);
		this.kieSession.fireAllRules();
		assertEquals(this.kieSession.getObjects(new ClassObjectFilter(RenewBillEvent.class)).size(), 0);
		assertEquals(this.kieSession.getObjects(new ClassObjectFilter(SuspiciousClientEvent.class)).size(), 0);
	}
	
}
