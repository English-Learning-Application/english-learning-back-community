package com.security.app.services

import com.security.app.entities.ChatMessage
import com.security.app.entities.ChatSession
import com.security.app.model.ChatMessageType
import com.security.app.model.ChatType
import com.security.app.repositories.ChatMessageRepository
import com.security.app.repositories.ChatSessionRepository
import com.security.app.repositories.MessageUserRepository
import com.security.app.utils.LimitOffsetPageable
import com.security.app.utils.toUUID
import jakarta.transaction.Transactional
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class ChatMessageService(
    private val chatMessageRepository: ChatMessageRepository,
    private val chatSessionRepository: ChatSessionRepository,
    private val messageUserRepository: MessageUserRepository,
) {
    fun getMessages(sessionId: String, offset: Int, limit: Int) : List<ChatMessage> {
        val pageable = LimitOffsetPageable(limit, offset, Sort.by("sentAt").descending())
        val page = chatMessageRepository.findAllBySessionSessionId(sessionId.toUUID(), pageable)
        return page.content
    }

    @Transactional
    fun saveChatMessage(messageText: String, userId: String, sessionId: String) : com.security.app.entities.ChatMessage {
        val messageUser = messageUserRepository.findByExternalUserId(userId)

        val chatSession = chatSessionRepository.findBySessionId(sessionId.toUUID())

        val chatMessage = ChatMessage().let {
            it.message = messageText
            it.sender = messageUser
            it.session = chatSession
            it.chatMessageType = ChatMessageType.TEXT
            it
        }

        return chatMessageRepository.save(chatMessage)
    }

    @Transactional
    fun saveAiResponse(messageText: String, sessionId: String) : com.security.app.entities.ChatMessage {
        val aiUser = messageUserRepository.findByExternalUserId("AI")

        val chatSession = chatSessionRepository.findBySessionId(sessionId.toUUID())

        val chatMessage = ChatMessage().let {
            it.message = messageText
            it.sender = aiUser
            it.session = chatSession
            it
        }

        return chatMessageRepository.save(chatMessage)
    }
}