package com.example.demo.controller;

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

import com.example.demo.dto.BillDTO;
import com.example.demo.rules.BillRequest;
import com.example.demo.rules.BillResponse;
import com.example.demo.rules.CloseResponse;
import com.example.demo.rules.IncreaseResponse;
import com.example.demo.rules.RenewalResponse;
import com.example.demo.rules.ReportResponse;
import com.example.demo.service.BillService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/bills", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasAuthority('KLIJENT')")	
public class BillController {

	private final BillService billService;
			
	@GetMapping
	public ResponseEntity<Page<BillDTO>> findAll(@RequestParam boolean rsd, Pageable pageable, @RequestParam String search){
		return ResponseEntity.ok(this.billService.findAll(rsd, pageable, search).map(BillDTO::new));
	}

	@PostMapping
	public ResponseEntity<BillResponse> create(@Valid @RequestBody BillRequest request){
		return ResponseEntity.ok(this.billService.create(request));
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<CloseResponse> delete(@PathVariable long id){
		return ResponseEntity.ok(this.billService.close(id));
	}

	@PostMapping(value = "/terms")
	public ResponseEntity<BillResponse> terms(@Valid @RequestBody BillRequest request){
		return ResponseEntity.ok(this.billService.terms(request));
	}

	@PutMapping(value = "/{id}/increase/{amount}")
	public ResponseEntity<IncreaseResponse> increase(@PathVariable long id, @PathVariable int amount){
		return ResponseEntity.ok(this.billService.increase(id, amount));
	}

	@PutMapping(value = "/{id}/renew/{amount}")
	public ResponseEntity<RenewalResponse> renew(@PathVariable long id, @PathVariable int amount){
		return ResponseEntity.ok(this.billService.renew(id, amount));
	}

	@GetMapping(value = "/report")
	@PreAuthorize("hasAuthority('SLUZBENIK')")	
	public ResponseEntity<ReportResponse> report(){
		return ResponseEntity.ok(this.billService.report());
	}

}
