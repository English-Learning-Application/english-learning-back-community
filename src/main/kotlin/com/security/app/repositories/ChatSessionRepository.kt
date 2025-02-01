package com.security.app.repositories

import com.security.app.entities.ChatSession
import com.security.app.model.ChatType
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ChatSessionRepository : JpaRepository<ChatSession, UUID> {
    fun findAllBySessionId(sessionId: UUID): List<ChatSession>
    fun findBySessionId(sessionId: UUID): ChatSession?
    fun findAllBySessionTypeAndUsers_UserId(chatType: ChatType, userId: UUID): List<ChatSession>
}