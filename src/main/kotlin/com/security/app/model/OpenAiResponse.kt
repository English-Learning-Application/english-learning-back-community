package com.security.app.model

data class ChatMessage(val role: String, val content: String)
data class OpenAiRequest(val model: String, val messages: List<ChatMessage>, val temperature: Double)
data class Choice(val message: ChatMessage)
data class OpenAiResponse(val choices: List<Choice>)