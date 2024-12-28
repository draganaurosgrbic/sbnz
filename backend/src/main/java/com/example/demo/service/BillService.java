package com.example.demo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.rules.BillRequest;
import com.example.demo.rules.BillResponse;
import com.example.demo.rules.CloseResponse;
import com.example.demo.rules.IncreaseResponse;
import com.example.demo.rules.RenewalResponse;
import com.example.demo.rules.ReportResponse;
import com.example.demo.events.CloseBillEvent;
import com.example.demo.events.CreateBillEvent;
import com.example.demo.events.IncreaseBillEvent;
import com.example.demo.events.RenewBillEvent;
import com.example.demo.model.Account;
import com.example.demo.model.Bill;
import com.example.demo.model.BillStatus;
import com.example.demo.model.Renewal;
import com.example.demo.model.Transaction;
import com.example.demo.repository.BillRepository;

import lombok.AllArgsConstructor;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class BillService {
		
	private final BillRepository billRepository;
	private final UserService userService;
	private final AccountService accountService;
	private final TransactionService transactionService;
	private final RenewalService renewalService;
	private final ResonerService resonerService;
	private final EventService eventService;
	private final ExchangeRateService exchangeRateService;
		
	public Page<Bill> findAll(boolean rsd, Pageable pageable, String search) {
		return this.billRepository.findAll(this.userService.currentUser().getId(), rsd, pageable, search);
	}

	public BillResponse terms(BillRequest request) {
		return this.resonerService.createBill(this.userService.currentUser().getAccount(), request);
	}

	@Transactional(readOnly = false)
	public BillResponse create(BillRequest request) {
		BillResponse response = this.terms(request);

		if (response.isValid()) {
			Account account = this.userService.currentUser().getAccount();
			account.setBalance(account.getBalance() 
				- this.exchangeRateService.convertToRSD(request.getType(), request.getBase()));
			this.accountService.save(account);
			Bill bill = new Bill(account, request, response, 
				this.exchangeRateService.convertToCurrency(request.getType(), response.getReward()));
			this.billRepository.save(bill);
			this.eventService.addEvent(new CreateBillEvent(bill));
		}
		
		return response;
	}

	@Transactional(readOnly = false)
	public IncreaseResponse increase(long id, double amount) {
		Bill bill = this.billRepository.findById(id).get();
		this.checkEnabled(bill);
		IncreaseResponse response = this.resonerService.increaseBill(bill, amount);
		
		if (response.isValid()) {
			Account account = this.userService.currentUser().getAccount();
			account.setBalance(account.getBalance() 
				- this.exchangeRateService.convertToRSD(bill.getType(), amount));
			this.accountService.save(account);
			bill.setBase(bill.getBase() + response.getBaseUpdate());
			bill.setInterest(bill.getInterest() - response.getInterestUpdate());
			this.billRepository.save(bill);
			Transaction transaction = new Transaction(bill, amount);
			this.transactionService.save(transaction);
			this.eventService.addEvent(new IncreaseBillEvent(transaction));
		}

		return response;
	}
	
	@Transactional(readOnly = false)
	public RenewalResponse renew(long id, int amount) {
		Bill bill = this.billRepository.findById(id).get();
		this.checkEnabled(bill);
		RenewalResponse response = this.resonerService.renewBill(bill, amount);
		
		if (response.isValid()) {
			bill.setInterest(bill.getInterest() + response.getInterestUpdate());
			bill.setEndDate(bill.getEndDate().plusMonths(amount));
			this.billRepository.save(bill);
			Renewal renewal = new Renewal(bill, amount);
			this.renewalService.save(renewal);
			this.eventService.addEvent(new RenewBillEvent(renewal));
		}

		return response;
	}

	@Transactional(readOnly = false)
	public CloseResponse close(long id) {
		Bill bill = this.billRepository.findById(id).get();
		this.checkEnabled(bill);
		CloseResponse response = this.resonerService.closeBill(bill);

		if (response.isValid()) {
			Account account = bill.getAccount();
			account.setBalance(account.getBalance() 
				+ this.exchangeRateService.convertToRSD(bill.getType(), bill.close()));
			this.accountService.save(account);
			this.billRepository.save(bill);
			this.eventService.addEvent(new CloseBillEvent(bill));
		}

		return response;
	}

	public ReportResponse report() {
		return this.resonerService.baseReport(this.billRepository.findAll());
	}

	private void checkEnabled(Bill bill) {
		if (!bill.getStatus().equals(BillStatus.ACTIVE) ||
				bill.getAccount().getId() != this.userService.currentUser().getAccount().getId()) {
			throw new RuntimeException();
		}
	}

}
