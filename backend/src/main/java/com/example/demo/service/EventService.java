package com.example.demo.service;

import java.util.HashMap;
import java.util.Map;

import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Service;

import com.example.demo.events.Event;
import com.example.demo.model.Account;
import com.example.demo.utils.Constants;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EventService {

	private final NotificationService notificationService;
	private final KieContainer kieContainer;
	private final Map<Long, KieSession> kieSessions = new HashMap<>();
	
	public void addEvent(Event event) {
		long key = event.getAccount().getId();
		if (!this.kieSessions.containsKey(key)) {
			this.initSession(event.getAccount());
		}
		this.kieSessions.get(key).insert(event);
	}
	
	private void initSession(Account account) {
		KieSession kieSession = this.kieContainer.newKieSession(Constants.EVENT_RULES_REALTIME);
		kieSession.setGlobal("notificationService", this.notificationService);
		kieSession.setGlobal("account", account);
        new Thread(() -> kieSession.fireUntilHalt()).start();
        this.kieSessions.put(account.getId(), kieSession);
	}
	
}
