package com.example.demo.dto;

import java.util.Date;

import com.example.demo.model.Notification;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NotificationDTO {

	private long id;
	private Date date;
	private String message;

	public NotificationDTO(Notification notification) {
		super();
		this.id = notification.getId();
		this.date = notification.getDate();
		this.message = notification.getMessage();
	}

}
