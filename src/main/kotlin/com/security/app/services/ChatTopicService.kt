package com.security.app.services

import com.security.app.entities.ChatTopic
import com.security.app.repositories.ChatTopicRepository
import org.springframework.stereotype.Service

@Service
class ChatTopicService(
    private val chatTopicRepository: ChatTopicRepository
) {
    fun getChatTopics() : List<ChatTopic> {
        return chatTopicRepository.findAll()
    }
}