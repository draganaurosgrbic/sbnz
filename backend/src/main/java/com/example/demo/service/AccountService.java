package com.example.demo.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.Account;
import com.example.demo.model.BillStatus;
import com.example.demo.repository.AccountRepository;

import lombok.AllArgsConstructor;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class AccountService {

	private final AccountRepository accountRepository;
	private final UserService userService;
	private final ResonerService resonserService;
		
	public Page<Account> findAll(Pageable pageable, String search) {
		return this.accountRepository.findAll(pageable, search);
	}

	public Account findOne() {
		return this.accountRepository.findByUserId(this.userService.currentUser().getId());
	}

	@Transactional(readOnly = false)
	public Account save(Account account) {
		this.userService.save(account.getUser());
		return this.accountRepository.save(account);
	}

	@Transactional(readOnly = false)
	public void delete(long id) {
		Account account = this.accountRepository.findById(id).get();
		if (account.getBills().stream().filter(
				bill -> bill.getStatus().equals(BillStatus.ACTIVE)).count() > 0) {
			throw new RuntimeException();
		}
		this.accountRepository.deleteById(id);
	}
	
	public List<Account> report(int index){
		return this.resonserService.advancedReport(this.accountRepository.findAll(), index);
	}

}
