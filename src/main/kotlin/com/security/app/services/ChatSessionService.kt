package com.security.app.services

import com.security.app.entities.ChatMessage
import com.security.app.entities.ChatSession
import com.security.app.model.ChatMessageType
import com.security.app.model.ChatType
import com.security.app.repositories.ChatMessageRepository
import com.security.app.repositories.ChatSessionRepository
import com.security.app.repositories.ChatTopicRepository
import com.security.app.utils.toUUID
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class ChatSessionService(
    private val chatMessageRepository: ChatMessageRepository,
    private val chatSessionRepository: ChatSessionRepository,
    private val messageUserService: MessageUserService,
    private val chatTopicRepository: ChatTopicRepository,
) {
    fun getSessionsByTypeAndUserId(chatType: String, userId: String): List<ChatSession> {
        val chatTypeEnum = ChatType.fromServerValue(chatType)

        return chatSessionRepository.findAllBySessionTypeAndUsers_ExternalUserId(chatTypeEnum, userId)
    }

    @Transactional
    fun createChatSession(chatType: String, userId: String, tokenString: String): ChatSession {
        val chatTypeEnum = ChatType.fromServerValue(chatType)
        var messageUser = messageUserService.getSessionUser(userId)
        if (messageUser == null) {
            messageUser = messageUserService.createUser(userId, tokenString)
        }

        val chatSession = ChatSession().let {
            it.sessionType = chatTypeEnum
            it.users = mutableSetOf(messageUser)
            it
        }

        return chatSessionRepository.save(chatSession)
    }

    @Transactional
    fun createGroupChatSession(userId: String, topicId: String, tokenString: String, sessionName: String) : ChatSession {
        val chatType = ChatType.GROUP
        var messageUser = messageUserService.getSessionUser(userId)
        if (messageUser == null) {
            messageUser = messageUserService.createUser(userId, tokenString)
        }

        val chatTopic = chatTopicRepository.findChatTopicByTopicId(topicId.toUUID()) ?: throw IllegalArgumentException("Topic not found")

        val chatSession = ChatSession().let {
            it.sessionType = chatType
            it.users = mutableSetOf(messageUser)
            it.topic = chatTopic
            it
        }

        val savedChatSession = chatSessionRepository.save(chatSession)
        val chatMessage = ChatMessage().let {
            it.message = "User joined the chat"
            it.chatMessageType = ChatMessageType.JOIN
            it.sender = messageUser
            it.session = chatSession
            it
        }

        chatMessageRepository.save(chatMessage)

        return savedChatSession
    }

    @Transactional
    fun deleteChatSession(sessionId: String) {
        chatSessionRepository.deleteBySessionId(sessionId.toUUID())
    }

    fun getSessionsByTopicId(topicId: String): List<ChatSession> {
        return chatSessionRepository.findAllByTopic_TopicId(topicId.toUUID())
    }

    fun getPrivateChatSessionsOfUser(
        userId: String
    ): List<ChatSession> {
        return chatSessionRepository.findAllBySessionTypeAndUsers_ExternalUserId(ChatType.PRIVATE, userId)
    }
}