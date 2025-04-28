package com.mysite.notificationservice.domain.sse.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.mysite.notificationservice.domain.sse.SseEmitterManager;
import com.mysite.notificationservice.domain.sse.controller.dto.NotificationRequest;

@ExtendWith(MockitoExtension.class)
public class SseControllerTest {

	private MockMvc mockMvc;

	@Mock
	private SseEmitterManager sseEmitterManager;

	@InjectMocks
	private SseController sseController;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(sseController)
			.defaultRequest(post("/send")
				.contentType(MediaType.APPLICATION_JSON) // 모든 요청의 Content-Type을 application/json으로 설정
				.characterEncoding("UTF-8")) // UTF-8 인코딩 설정
			.build();
	}

	@Test
	@DisplayName("알림 전송 성공")
	void t1() throws Exception {
		String userId = "user123";
		String message = "Test Notification";
		NotificationRequest request = new NotificationRequest(userId, message);

		when(sseEmitterManager.getEmitter(userId)).thenReturn(new SseEmitter());

		ResultActions perform = mockMvc.perform(post("/send")
			.content("{\"userId\":\"user123\", \"message\":\"Test Notification\"}"));

		perform.andExpect(status().isOk())
			.andExpect(jsonPath("$.result").value("알림이 성공적으로 전송되었습니다."));
	}

	@Test
	@DisplayName("알림 전송 실패 - 구독자 없음")
	void t2() throws Exception {
		String userId = "user123";
		String message = "Test Notification";
		NotificationRequest request = new NotificationRequest(userId, message);

		when(sseEmitterManager.getEmitter(userId)).thenReturn(null);

		mockMvc.perform(post("/send")
				.content("{\"userId\":\"user123\", \"message\":\"Test Notification\"}"))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.result").value("유저 ID에 해당하는 구독자가 없습니다."));
	}

	@Test
	@DisplayName("알림 전송 실패 - IOException 발생")
	void t3() throws Exception {
		String userId = "user123";
		String message = "Test Notification";
		NotificationRequest request = new NotificationRequest(userId, message);

		SseEmitter emitter = mock(SseEmitter.class);
		when(sseEmitterManager.getEmitter(userId)).thenReturn(emitter);

		doThrow(new IOException("Test exception"))
			.when(emitter)
			.send(message);

		mockMvc.perform(post("/send")
				.content("{\"userId\":\"user123\", \"message\":\"Test Notification\"}"))
			.andExpect(status().isInternalServerError())
			.andExpect(jsonPath("$.result").value("알림 전송 중 오류가 발생했습니다."));
	}
}

