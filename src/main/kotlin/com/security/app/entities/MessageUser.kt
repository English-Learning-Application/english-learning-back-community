package com.security.app.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import lombok.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import java.util.*

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
@EntityListeners(AuditingEntityListener::class)
class MessageUser {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    lateinit var userId: UUID

    @Column(nullable = false)
    var username: String = ""

    @Column(nullable = true)
    var imageUrl: String? = null

    @Column(nullable = false)
    var email: String = ""

    @Column(nullable = false)
    var phoneNumber: String = ""

    @Column(nullable = true)
    var externalUserId: String? = null

    @OneToMany(mappedBy = "sender", cascade = [CascadeType.ALL])
    @JsonIgnore
    var messages: MutableSet<ChatMessage> = mutableSetOf()

    @ManyToMany(mappedBy = "users")
    @JsonIgnore
    var chatSessions: MutableSet<ChatSession> = mutableSetOf()

    @CreatedDate
    @Column(nullable = false, updatable = false)
    var createdAt: LocalDateTime = LocalDateTime.now()

    @LastModifiedDate
    @Column(nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
}