package com.security.app.entities

import com.security.app.model.Language
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
class ChatTopic {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    lateinit var topicId: UUID

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var topicLanguage: Language = Language.ENGLISH

    @Column(nullable = false)
    var topicNameEnglish: String = ""

    @Column(nullable = false)
    var topicNameVietnamese: String = ""

    @Column(nullable = false)
    var topicNameFrench: String = ""

    @Column(nullable = false)
    var topicDescription: String = ""

    @OneToMany(mappedBy = "topic", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    var sessions: Set<ChatSession> = HashSet()

    @CreatedDate
    @Column(nullable = false, updatable = false)
    var createdAt: LocalDateTime = LocalDateTime.now()

    @LastModifiedDate
    @Column(nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
}