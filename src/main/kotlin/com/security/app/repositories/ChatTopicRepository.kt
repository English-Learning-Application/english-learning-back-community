package com.security.app.repositories

import com.security.app.entities.ChatTopic
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ChatTopicRepository : JpaRepository<ChatTopic, UUID> {
    fun findChatTopicByTopicId(topicId: UUID): ChatTopic?
}