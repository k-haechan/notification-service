package com.mysite.notificationservice.domain.sse.controller.dto;

public record NotificationRequest(
	String userId,
	String message
) {
}
