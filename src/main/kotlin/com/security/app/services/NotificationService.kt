package com.security.app.services

import com.security.app.model.ListMessage
import com.security.app.request.SendNotificationMessage
import com.security.app.request.SendNotificationRequest
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class NotificationService(
    private val webClient: WebClient
) {
    private final val NOTIFICATION_SERVICE_URL = System.getenv("NOTIFICATION_SERVICE_URL")

    fun sendNotification(
        notificationType: String,
        channels: List<String>,
        receiverId: String,
        message: SendNotificationMessage
    ) {
        webClient.post()
            .uri("$NOTIFICATION_SERVICE_URL/send")
            .bodyValue(
                SendNotificationRequest(
                    notificationType,
                    channels,
                    receiverId,
                    message
                )
            )
            .retrieve()
            .bodyToMono(ListMessage.Success::class.java)
            .subscribe()
    }
}