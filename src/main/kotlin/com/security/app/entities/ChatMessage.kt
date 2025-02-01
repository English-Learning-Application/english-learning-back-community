package com.security.app.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import lombok.*
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
class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    lateinit var messageId: UUID

    @Column(nullable = false, columnDefinition = "TEXT DEFAULT ''")
    var message: String = ""

    @ManyToOne
    @JoinColumn(name = "session_id")
    @JsonIgnore
    var session: ChatSession? = null

    @ManyToOne
    @JoinColumn(name = "sender_id")
    var sender: MessageUser? = null

    @Column(nullable = false)
    var sentAt: LocalDateTime = LocalDateTime.now()
}