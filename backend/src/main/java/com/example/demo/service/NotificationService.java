package com.example.demo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.Notification;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.NotificationRepository;

import lombok.AllArgsConstructor;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class NotificationService {

	private final NotificationRepository notificationRepository;
	private final UserService userService;
	
	public Page<Notification> findAll(Pageable pageable) {
		User user = this.userService.currentUser();
		if (user.getAuthority().getName().equals(Role.ADMIN)) {
			return this.notificationRepository.findByAccountIsNullOrderByDateDesc(pageable);			
		}
		return this.notificationRepository.findByAccountIdOrderByDateDesc(pageable, user.getAccount().getId());
	}

	@Transactional(readOnly = false)
	public void save(Notification notification) {
		this.notificationRepository.save(notification);
	}
	
	@Transactional(readOnly = false)
	public void delete(long id) {
		this.notificationRepository.deleteById(id);
	}
	
}
