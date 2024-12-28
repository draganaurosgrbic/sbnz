package com.example.demo.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.AccountDTO;
import com.example.demo.mapper.AccountMapper;
import com.example.demo.service.AccountService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/accounts", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasAuthority('SLUZBENIK')")	
public class AccountController {
	
	private final AccountService accountService;
	private final AccountMapper accountMapper;
	
	@GetMapping
	public ResponseEntity<Page<AccountDTO>> findAll(Pageable pageable, @RequestParam String search){
		return ResponseEntity.ok(this.accountService.findAll(pageable, search).map(AccountDTO::new));
	}
	
	@PreAuthorize("hasAuthority('KLIJENT')")	
	@GetMapping(value = "/my")
	public ResponseEntity<AccountDTO> findOne(){
		return ResponseEntity.ok(new AccountDTO(this.accountService.findOne()));
	}

	@PostMapping
	public ResponseEntity<AccountDTO> create(@Valid @RequestBody AccountDTO accountDTO){
		return ResponseEntity.ok(new AccountDTO(this.accountService.save(this.accountMapper.map(accountDTO))));
	}
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<AccountDTO> update(@PathVariable long id, @Valid @RequestBody AccountDTO accountDTO){
		return ResponseEntity.ok(new AccountDTO(this.accountService.save(this.accountMapper.map(id, accountDTO))));
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable long id){
		this.accountService.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping(value = "/report-1")
	public ResponseEntity<List<AccountDTO>> firstReport(){
		return ResponseEntity.ok(this.accountMapper.map(this.accountService.report(1)));
	}
		
	@GetMapping(value = "/report-2")
	public ResponseEntity<List<AccountDTO>> secondReport(){
		return ResponseEntity.ok(this.accountMapper.map(this.accountService.report(2)));
	}

	@GetMapping(value = "/report-3")
	public ResponseEntity<List<AccountDTO>> thirdReport(){
		return ResponseEntity.ok(this.accountMapper.map(this.accountService.report(3)));
	}

}
