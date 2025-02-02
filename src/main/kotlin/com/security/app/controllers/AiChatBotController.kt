package com.security.app.controllers

import com.security.app.entities.ChatMessage
import com.security.app.entities.ChatSession
import com.security.app.model.ChatType
import com.security.app.model.ListMessage
import com.security.app.model.Message
import com.security.app.services.ChatMessageService
import com.security.app.services.ChatSessionService
import jakarta.servlet.http.HttpServletRequest
import jakarta.websocket.server.PathParam
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/chatbot")
class AiChatBotController(
    private val chatSessionService: ChatSessionService,
    private val chatMessageService: ChatMessageService,
){
    @GetMapping("/sessions")
    fun getChatSessions() : ResponseEntity<ListMessage<ChatSession>> {
        val authentication = SecurityContextHolder.getContext().authentication
        val userId = authentication.name

        val chatSessions = chatSessionService.getSessionsByTypeAndUserId(ChatType.AI_BOT.serverValue, userId)
        return ResponseEntity.ok(ListMessage.Success("Chat sessions fetched", chatSessions))
    }

    @PostMapping("/sessions")
    fun createChatSession(
        request: HttpServletRequest,
    ) : ResponseEntity<Message<ChatSession>> {
        val authentication = SecurityContextHolder.getContext().authentication
        val userId = authentication.name

        val chatSession = chatSessionService.createChatSession(ChatType.AI_BOT.serverValue, userId, request.getHeader("Authorization"))
        return ResponseEntity.ok(Message.Success("Chat session created", chatSession))
    }

    @DeleteMapping("/sessions/{sessionId}")
    fun deleteChatSession(
        @PathVariable("sessionId") sessionId: String,
    ) : ResponseEntity<Message<Any>> {
        chatSessionService.deleteChatSession(sessionId)
        return ResponseEntity.ok(Message.Success("Chat session deleted", {  }))
    }

    @GetMapping("/sessions/{sessionId}/messages")
    fun getChatMessages(
        @PathVariable("sessionId") sessionId: String,
        @RequestParam("offset", defaultValue = "0") offset: Int,
        @RequestParam("limit", defaultValue = "10") limit: Int,
    ) : ResponseEntity<ListMessage<ChatMessage>> {

        val chatMessages = chatMessageService.getMessages(sessionId, offset, limit)
        return ResponseEntity.ok(ListMessage.Success("Chat messages fetched", chatMessages))
    }
}