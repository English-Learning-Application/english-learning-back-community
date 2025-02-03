package com.security.app.services

import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class NotificationService(
    private val webClient: WebClient
) {
    private final val NOTIFICATION_SERVICE_URL = System.getenv("NOTIFICATION_SERVICE_URL")

    fun sendNotification() {
        webClient.post()
            .uri("$NOTIFICATION_SERVICE_URL/send")
            .retrieve()
            .bodyToMono(String::class.java)
            .block()
    }
}