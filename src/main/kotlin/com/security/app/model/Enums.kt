package com.security.app.model

enum class ChatMessageType(val serverValue: String) {
    TEXT("TEXT"),
    IMAGE("IMAGE"),
    VIDEO("VIDEO"),
    JOIN("JOIN"),
    LEAVE("LEAVE");

    companion object {
        fun fromServerValue(serverValue: String): ChatMessageType {
            return when (serverValue) {
                "TEXT" -> TEXT
                "IMAGE" -> IMAGE
                "VIDEO" -> VIDEO
                "JOIN" -> JOIN
                "LEAVE" -> LEAVE
                else -> throw IllegalArgumentException("Invalid value $serverValue")
            }
        }
    }
}

enum class ChatType(val serverValue: String) {
    AI_BOT("AI_BOT"),
    PRIVATE("PRIVATE"),
    GROUP("GROUP");

    companion object {
        fun fromServerValue(serverValue: String): ChatType {
            return when (serverValue) {
                "AI_BOT" -> AI_BOT
                "PRIVATE" -> PRIVATE
                "GROUP" -> GROUP
                else -> throw IllegalArgumentException("Invalid value $serverValue")
            }
        }
    }
}