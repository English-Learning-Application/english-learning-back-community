package com.security.app.services

import com.security.app.model.ChatMessage
import com.security.app.model.OpenAiRequest
import com.security.app.model.OpenAiResponse
import net.minidev.json.JSONObject
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Service
class AiChatService(
    private val webClient: WebClient,
) {
    private final val OPEN_AI_API_KEY = System.getenv("OPEN_AI_API_KEY")

    fun getChatResponse(message: String): Mono<String> {
        val requestBody = OpenAiRequest(
            model = "gpt-4",
            messages = listOf(
                ChatMessage("system", "You are an AI assistant."),
                ChatMessage("user", message)
            ),
            temperature = 0.7
        )

        val resp =  webClient.post()
            .uri("https://api.openai.com/v1/chat/completions")
            .header("Authorization", "Bearer $OPEN_AI_API_KEY")
            .header("Content-Type", "application/json")
            .bodyValue(requestBody)
            .retrieve()
            .bodyToMono(OpenAiResponse::class.java)
            .map { response -> response.choices.firstOrNull()?.message?.content ?: "No response from AI." }
            .onErrorReturn("Sorry, I couldn't process your request.")
        return resp
    }
}