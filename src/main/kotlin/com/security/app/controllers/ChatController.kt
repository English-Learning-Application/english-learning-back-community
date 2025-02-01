package com.security.app.controllers

import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller
import java.security.Principal

@Controller
class ChatController(
    private val simpMessagingTemplate: SimpMessagingTemplate,
) {
    @MessageMapping("/chat")
    fun sendMessage(@Payload message: String, principal: Principal) {
        principal.name
        simpMessagingTemplate.convertAndSend("/topic/messages", "$message from ${principal.name}")
    }
}