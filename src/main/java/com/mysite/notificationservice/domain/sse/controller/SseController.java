package com.mysite.notificationservice.domain.sse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.mysite.notificationservice.domain.sse.SseEmitterManager;

/**
 * PackageName : com.mysite.notificationservice.domain.notification.controller
 * FileName    : SseController
 * Author      : hc
 * Date        : 25. 4. 28.
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 4. 28.     hc               Initial creation
 */
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
}
