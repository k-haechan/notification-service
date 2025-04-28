package com.mysite.notificationservice.domain.notification.subscriber;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.mysite.notificationservice.domain.sse.SseEmitterManager;

@Component
public class NotificationSubscriber implements MessageListener {

	private final SseEmitterManager sseEmitterManager;

	@Autowired
	public NotificationSubscriber(SseEmitterManager sseEmitterManager) {
		this.sseEmitterManager = sseEmitterManager;
	}

	@Override
	public void onMessage(Message message, byte[] pattern) {
		String payload = new String(message.getBody());

		// payload = "userId|message" 형태로 되어있다고 가정
		String[] parts = payload.split("\\|", 2);
		if (parts.length != 2) {
			return; // 잘못된 포맷이면 무시
		}

		String userId = parts[0];
		String notificationMessage = parts[1];

		SseEmitter emitter = sseEmitterManager.getEmitter(userId);
		if (emitter != null) {
			try {
				emitter.send(notificationMessage);
			} catch (IOException e) {
				emitter.completeWithError(e);
				sseEmitterManager.removeEmitter(userId);
			}
		}
	}
}
