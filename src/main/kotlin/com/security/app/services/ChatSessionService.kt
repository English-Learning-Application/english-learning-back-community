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
import org.springframework.messaging.simp.SimpMessagingTemplate
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
    fun joinChatSession(userId: String, sessionId: String, tokenString: String, simpMessagingTemplate: SimpMessagingTemplate): ChatSession {
        var messageUser = messageUserService.getSessionUser(userId)
        if (messageUser == null) {
            messageUser = messageUserService.createUser(userId, tokenString)
        }

        val chatSession = chatSessionRepository.findBySessionId(sessionId.toUUID()) ?: throw IllegalArgumentException("Session not found")

        chatSession.users.add(messageUser)

        val savedChatSession = chatSessionRepository.save(chatSession)

        val chatMessage = ChatMessage().let {
            it.message = "User joined the chat"
            it.chatMessageType = ChatMessageType.JOIN
            it.sender = messageUser
            it.session = savedChatSession
            it
        }

        val savedChatMessage = chatMessageRepository.save(chatMessage)

        simpMessagingTemplate.convertAndSend("/group/$sessionId", savedChatMessage)
        return savedChatSession
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
            it.sessionName = sessionName
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

    @Transactional
    fun generatePrivateChatSessionWithUser(
        userId: String,
        receiverId: String,
        tokenString: String
    ): ChatSession {

        println("The Receiver ID: $receiverId")
        println("The User ID: $userId")
        val existingChatSessions = chatSessionRepository.findBySessionTypeAndUsers_ExternalUserIdIn(ChatType.PRIVATE, listOf(userId, receiverId))

        val existingChatSession = existingChatSessions.find { session ->
            session.users.map { it.externalUserId }.containsAll(listOf(userId, receiverId)).and(session.users.size == 2)
        }

        println("Existing chat session: ${existingChatSession?.sessionId}")
        println("Existing chat session: ${existingChatSession?.users?.map { it.externalUserId }}")

        if (existingChatSession != null) {
            return existingChatSession
        }

        var messageUser = messageUserService.getSessionUser(userId)
        var receiverMessageUser = messageUserService.getSessionUser(receiverId)


        if (messageUser == null && receiverMessageUser == null) {
           val resultList = messageUserService.createListOfMessageUser(listOf(userId, receiverId), tokenString)
            messageUser = resultList.find { it.externalUserId == userId }
            receiverMessageUser = resultList.find { it.externalUserId == receiverId }
        } else if (messageUser == null) {
            messageUser = messageUserService.createUser(userId, tokenString)
        } else if (receiverMessageUser == null) {
            receiverMessageUser = messageUserService.createUser(receiverId, tokenString)
        }


        val chatSession = ChatSession().let {
            it.users = mutableSetOf(messageUser!!, receiverMessageUser!!)
            it.sessionType = ChatType.PRIVATE
            it
        }

        val savedChatSession = chatSessionRepository.save(chatSession)

        return savedChatSession
    }
}