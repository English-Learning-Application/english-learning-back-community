package com.security.app.repositories

import com.security.app.entities.ChatMessage
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ChatMessageRepository : JpaRepository<ChatMessage, UUID> {
    fun findAllBySessionSessionId(sessionId: UUID, pageable: Pageable): Page<ChatMessage>
}