package com.mysite.notificationservice.domain.sse.controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.mysite.notificationservice.domain.sse.SseEmitterManager;
import com.mysite.notificationservice.domain.sse.controller.dto.NotificationRequest;

@RestController
public class SseController {

	private final SseEmitterManager sseEmitterManager;

	@Autowired
	public SseController(SseEmitterManager sseEmitterManager) {
		this.sseEmitterManager = sseEmitterManager;
	}

	@GetMapping("/subscribe/{userId}")
	public SseEmitter subscribe(@PathVariable String userId) {
		return sseEmitterManager.subscribe(userId);
	}

	@PostMapping(value = "/send")
	public ResponseEntity<Map<String, Object>> sendNotification(@RequestBody NotificationRequest request) {
		SseEmitter emitter = sseEmitterManager.getEmitter(request.userId());
		if (emitter != null) {
			try {
				emitter.send(request.message());
				return ResponseEntity.ok(Map.of("result", "알림이 성공적으로 전송되었습니다."));
			} catch (IOException e) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("result", "알림 전송 중 오류가 발생했습니다."));
			}
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("result", "유저 ID에 해당하는 구독자가 없습니다."));
		}
	}
}
