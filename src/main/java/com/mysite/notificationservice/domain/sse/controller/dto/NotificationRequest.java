package com.mysite.notificationservice.domain.sse.controller.dto;

/**
 * PackageName : com.mysite.notificationservice.domain.sse.controller.dto
 * FileName    : NotificationRequest
 * Author      : hc
 * Date        : 25. 4. 28.
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 4. 28.     hc               Initial creation
 */
public record NotificationRequest(
	String userId,
	String message
) {
}
