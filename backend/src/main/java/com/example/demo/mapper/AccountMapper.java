package com.example.demo.mapper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.AccountDTO;
import com.example.demo.model.Account;
import com.example.demo.model.Authority;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.AuthorityRepository;
import com.example.demo.repository.UserRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class AccountMapper {

	private final AuthorityRepository authorityRepository;
	private final UserRepository userRepository;
	private final AccountRepository accountRepository;
	
	@Transactional(readOnly = true)
	public Account map(AccountDTO accountDTO) {
		User user = new User();
		Set<Authority> authorities = new HashSet<>();
		authorities.add(this.authorityRepository.findByName(Role.KLIJENT));
		user.setAuthorities(authorities);
		user.setPassword(UUID.randomUUID().toString());
		Account account = new Account();
		account.setBalance(1000000);
		user.setAccount(account);
		account.setUser(user);
		this.setModel(user, account, accountDTO);
		return account;
	}
	
	@Transactional(readOnly = true)
	public Account map(long id, AccountDTO accountDTO) {
		User user = this.userRepository.findByAccountId(id);
		Account account = this.accountRepository.findById(id).get();
		this.setModel(user, account, accountDTO);
		return account;
	}

	public List<AccountDTO> map(List<Account> accounts) {
		return accounts.stream().map(AccountDTO::new).collect(Collectors.toList());
	}
	
	private void setModel(User user, Account account, AccountDTO accountDTO) {
		user.setEmail(accountDTO.getEmail());
		user.setFirstName(accountDTO.getFirstName());
		user.setLastName(accountDTO.getLastName());
		account.setJmbg(accountDTO.getJmbg());
		account.setBirthDate(accountDTO.getBirthDate());
		account.setAddress(accountDTO.getAddress());
		account.setCity(accountDTO.getCity());
		account.setZipCode(accountDTO.getZipCode());
	}
	
}
