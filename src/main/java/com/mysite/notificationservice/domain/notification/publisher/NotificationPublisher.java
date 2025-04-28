package com.mysite.notificationservice.domain.notification.publisher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class NotificationPublisher {

	private static final String CHANNEL_NAME = "notification";
	private final StringRedisTemplate redisTemplate;

	@Autowired
	public NotificationPublisher(StringRedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public void publish(String userId, String message) {
		// 예: "userId|message" 형태로 보내기
		String payload = userId + "|" + message;
		redisTemplate.convertAndSend(CHANNEL_NAME, payload);
	}
}
