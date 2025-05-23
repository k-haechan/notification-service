package com.mysite.notificationservice.domain.sse;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Component
public class SseEmitterManager {

	// 사용자별 SseEmitter 저장
	private final Map<String, SseEmitter> emitterMap = new ConcurrentHashMap<>();

	// 사용자별 SseEmitter를 추가하는 메서드
	private SseEmitter createEmitter(String userId) {
		SseEmitter emitter = new SseEmitter(60_000L);  // 타임아웃 60초

		// 타임아웃이나 연결 종료 시 Emitter 제거
		emitter.onTimeout(() -> removeEmitter(userId));
		emitter.onCompletion(() -> removeEmitter(userId));

		emitterMap.put(userId, emitter);
		return emitter;
	}

	// 사용자별 SseEmitter를 제거하는 메서드
	public void removeEmitter(String userId) {
		emitterMap.remove(userId);
	}

	// 사용자별 SseEmitter 가져오기
	public SseEmitter getEmitter(String userId) {
		return emitterMap.get(userId);
	}

	// 클라이언트의 SSE 구독 요청 처리 메서드
	public SseEmitter subscribe(String userId) {
		return createEmitter(userId);
	}
}
