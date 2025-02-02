package com.security.app.repositories

import com.security.app.entities.ChatSession
import com.security.app.model.ChatType
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ChatSessionRepository : JpaRepository<ChatSession, UUID> {
    fun findAllBySessionId(sessionId: UUID): List<ChatSession>
    fun findBySessionId(sessionId: UUID): ChatSession?
    fun findAllBySessionTypeAndUsers_ExternalUserId(chatType: ChatType, userId: String): List<ChatSession>
    fun deleteBySessionId(sessionId: UUID)
    fun findAllByTopic_TopicId(topicId: UUID): List<ChatSession>
}