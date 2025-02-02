package com.security.app.controllers

import com.security.app.entities.ChatSession
import com.security.app.entities.ChatTopic
import com.security.app.model.ListMessage
import com.security.app.model.Message
import com.security.app.request.CreateTopicSessionRequest
import com.security.app.services.ChatSessionService
import com.security.app.services.ChatTopicService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/communities")
class CommunityController(
    private val chatTopicService: ChatTopicService,
    private val chatSessionService: ChatSessionService,
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

    @GetMapping("/sessions")
    fun getPrivateChatSessions() : ResponseEntity<ListMessage<ChatSession>> {
        val authentication = SecurityContextHolder.getContext().authentication
        val userId = authentication.name

        val chatSessions = chatSessionService.getPrivateChatSessionsOfUser(userId)
        return ResponseEntity.ok(ListMessage.Success("Private chat sessions retrieved successfully", chatSessions))
    }
}