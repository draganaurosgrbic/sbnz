package com.example.demo.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.NotificationDTO;
import com.example.demo.service.NotificationService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/notifications", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasAnyAuthority('ADMIN','KLIJENT')")
public class NotificationController {
	
	private final NotificationService notificationService;
	
	@GetMapping
	public ResponseEntity<Page<NotificationDTO>> findAll(Pageable pageable){
		return ResponseEntity.ok(this.notificationService.findAll(pageable).map(NotificationDTO::new));
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable long id){
		this.notificationService.delete(id);
		return ResponseEntity.noContent().build();
	}
	
}
