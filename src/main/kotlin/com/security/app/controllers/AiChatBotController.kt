package com.security.app.controllers

import com.security.app.entities.ChatSession
import com.security.app.model.ChatType
import com.security.app.model.ListMessage
import com.security.app.services.ChatSessionService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/chatbot")
class AiChatBotController(
    private val chatSessionService: ChatSessionService
){
    @GetMapping("/sessions")
    fun getChatSessions() : ResponseEntity<ListMessage<ChatSession>> {
        val authentication = SecurityContextHolder.getContext().authentication
        val userId = authentication.name

        val chatSessions = chatSessionService.getSessionsByTypeAndUserId(ChatType.AI_BOT.serverValue, userId)
        return ResponseEntity.ok(ListMessage.Success("Chat sessions fetched", chatSessions))
    }
}