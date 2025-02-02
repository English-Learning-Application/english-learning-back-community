package com.security.app.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import com.security.app.model.ChatType
import jakarta.persistence.*
import lombok.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
@EntityListeners(AuditingEntityListener::class)
class ChatSession {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    lateinit var sessionId: UUID

    @Column(nullable = false)
    var sessionType: ChatType = ChatType.PRIVATE

    @Column(nullable = true)
    var sessionName: String? = null

    @ManyToMany
    @JoinTable(
        name = "chat_session_users",
        joinColumns = [JoinColumn(name = "session_id")],
        inverseJoinColumns = [JoinColumn(name = "user_id")]
    )
    var users: MutableSet<MessageUser> = mutableSetOf()

    @OneToMany(mappedBy = "session", cascade = [CascadeType.ALL])
    var messages: MutableSet<ChatMessage> = mutableSetOf()

    @ManyToOne
    @JoinColumn(name = "topic_id", nullable = true)
    @JsonIgnore
    var topic: ChatTopic? = null

    @CreatedDate
    @Column(nullable = false, updatable = false)
    var createdAt: LocalDateTime = LocalDateTime.now()

    @LastModifiedDate
    @Column(nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
}