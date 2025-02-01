package com.security.app.services

import com.security.app.entities.ChatMessage
import com.security.app.entities.ChatSession
import com.security.app.model.ChatType
import com.security.app.repositories.ChatMessageRepository
import com.security.app.repositories.ChatSessionRepository
import com.security.app.utils.toUUID
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class ChatMessageService(
    private val chatMessageRepository: ChatMessageRepository,
) {
    fun getMessages(sessionId: String, offset: Int, limit: Int) : List<ChatMessage> {
        val pageable = PageRequest.of(offset/limit, limit)
        val page = chatMessageRepository.findAllBySessionSessionIdOrderBySentAtDesc(sessionId.toUUID(), pageable)
        return page.content
    }
}