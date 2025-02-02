package com.security.app.controllers

import com.security.app.services.AiChatService
import com.security.app.services.ChatMessageService
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller
import java.security.Principal

@Controller
class ChatController(
    private val simpMessagingTemplate: SimpMessagingTemplate,
    private val chatMessageService: ChatMessageService,
    private val aiChatService: AiChatService,
) {
    @MessageMapping("/chat/ai/{sessionId}")
    fun sendMessage(@Payload message: String, principal: Principal, @DestinationVariable sessionId: String) {
        println("Received message: $message")
        val userId = principal.name

        val sentChatMessage = chatMessageService.saveChatMessage(message, userId, sessionId)

        simpMessagingTemplate.convertAndSendToUser(sessionId, "/ai-chat", sentChatMessage)

        aiChatService.getChatResponse(message)
            .subscribe { response ->
                val aiChatMessage = chatMessageService.saveAiResponse(response, sessionId)
                simpMessagingTemplate.convertAndSendToUser(sessionId, "/ai-chat", aiChatMessage)
            }
    }
}