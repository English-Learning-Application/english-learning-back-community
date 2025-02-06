package com.security.app.controllers

import com.security.app.entities.ChatMessage
import com.security.app.entities.ChatSession
import com.security.app.entities.ChatTopic
import com.security.app.model.ListMessage
import com.security.app.model.Message
import com.security.app.request.CreateTopicSessionRequest
import com.security.app.services.ChatMessageService
import com.security.app.services.ChatSessionService
import com.security.app.services.ChatTopicService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/communities")
class CommunityController(
    private val chatTopicService: ChatTopicService,
    private val chatSessionService: ChatSessionService,
    private val chatMessageService: ChatMessageService,
    private val simpMessagingTemplate: SimpMessagingTemplate
) {
    @GetMapping("/topics")
    fun getChatTopics() : ResponseEntity<ListMessage<ChatTopic>> {
        val chatTopics = chatTopicService.getChatTopics()
        return ResponseEntity.ok(ListMessage.Success("Chat topics retrieved successfully", chatTopics))
    }

    @GetMapping("/topics/{topicId}/sessions")
    fun getChatSessionsByTopicId(
        @PathVariable("topicId") topicId: String
    ) : ResponseEntity<ListMessage<ChatSession>> {
        val chatSessions = chatSessionService.getSessionsByTopicId(topicId)
        return ResponseEntity.ok(ListMessage.Success("Chat sessions retrieved successfully", chatSessions))
    }

    @GetMapping("/me/topics/{topicId}/sessions")
    fun getChatSessionsByTopicIdAndUserId(
        @PathVariable("topicId") topicId: String
    ) : ResponseEntity<ListMessage<ChatSession>> {
        val authentication = SecurityContextHolder.getContext().authentication
        val userId = authentication.name

        val chatSessions = chatSessionService.getSessionsByTopicIdAndUserId(topicId, userId)
        return ResponseEntity.ok(ListMessage.Success("Chat sessions retrieved successfully", chatSessions))
    }

    @PostMapping("/topics/{topicId}/sessions")
    fun createChatSession(
        request: HttpServletRequest,
        @RequestBody body: CreateTopicSessionRequest,
        @PathVariable("topicId") topicId: String,
    ) : ResponseEntity<Message<ChatSession>> {
        val authentication = SecurityContextHolder.getContext().authentication
        val userId = authentication.name

        val chatSession = chatSessionService.createGroupChatSession(userId, topicId, request.getHeader("Authorization"), body.sessionName)
        return ResponseEntity.ok(Message.Success("Chat session created successfully", chatSession))
    }

    @PostMapping("/sessions/{sessionId}")
    fun joinChatSession(
        request: HttpServletRequest,
        @PathVariable("sessionId") sessionId: String,
    ) : ResponseEntity<Message<ChatSession>> {
        val authentication = SecurityContextHolder.getContext().authentication
        val userId = authentication.name

        val chatSession = chatSessionService.joinChatSession(userId, sessionId, tokenString = request.getHeader("Authorization"), simpMessagingTemplate)
        return ResponseEntity.ok(Message.Success("Chat session joined successfully", chatSession))
    }

    @GetMapping("/sessions/{sessionId}/messages")
    fun getChatMessages(
        @PathVariable("sessionId") sessionId: String,
        @RequestParam("offset", defaultValue = "0") offset: Int,
        @RequestParam("limit", defaultValue = "10") limit: Int,
        ) : ResponseEntity<ListMessage<ChatMessage>> {
        val chatMessages = chatMessageService.getMessages(sessionId, offset, limit)
        return ResponseEntity.ok(ListMessage.Success("Chat messages retrieved successfully", chatMessages))
    }

    @GetMapping("/sessions")
    fun getPrivateChatSessions() : ResponseEntity<ListMessage<ChatSession>> {
        val authentication = SecurityContextHolder.getContext().authentication
        val userId = authentication.name

        val chatSessions = chatSessionService.getPrivateChatSessionsOfUser(userId)
        return ResponseEntity.ok(ListMessage.Success("Private chat sessions retrieved successfully", chatSessions))
    }

    @PostMapping("/sessions/private/{receiverId}")
    fun createPrivateChatSession(
        request: HttpServletRequest,
        @PathVariable("receiverId") receiverId: String,
    ) : ResponseEntity<Message<ChatSession>> {
        val authentication = SecurityContextHolder.getContext().authentication
        val userId = authentication.name

        val chatSession = chatSessionService.generatePrivateChatSessionWithUser(userId, receiverId, request.getHeader("Authorization"))
        return ResponseEntity.ok(Message.Success("Private chat session created successfully", chatSession))
    }
}