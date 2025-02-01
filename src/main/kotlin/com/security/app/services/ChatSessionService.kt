package com.security.app.services

import com.security.app.entities.ChatSession
import com.security.app.model.ChatType
import com.security.app.repositories.ChatSessionRepository
import com.security.app.utils.toUUID
import org.springframework.stereotype.Service

@Service
class ChatSessionService(
    private val chatSessionRepository: ChatSessionRepository
) {
    fun getSessionsByTypeAndUserId(chatType: String, userId: String) : List<ChatSession> {
        val chatTypeEnum = ChatType.fromServerValue(chatType)

        return chatSessionRepository.findAllBySessionTypeAndUsers_UserId(chatTypeEnum, userId.toUUID())
    }
}