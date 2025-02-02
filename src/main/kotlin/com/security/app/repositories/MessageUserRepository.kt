package com.security.app.repositories

import com.security.app.entities.MessageUser
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface MessageUserRepository : JpaRepository<MessageUser, UUID> {
    fun findByUserId(userId: UUID): MessageUser?
    fun findByExternalUserId(externalUserId: String): MessageUser?
}