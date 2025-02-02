package com.security.app.services

import com.nimbusds.jose.shaded.gson.Gson
import com.security.app.entities.MessageUser
import com.security.app.model.Message
import com.security.app.model.UserModel
import com.security.app.repositories.MessageUserRepository
import com.security.app.utils.toUUID
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class MessageUserService(
    private val messageUserRepository: MessageUserRepository,
    private val webClient: WebClient,
) {
    private val PROFILE_SERVICE_URL = System.getenv("PROFILE_SERVICE_URL")
    fun getUserInformation(tokenString: String): UserModel? {
        webClient.get()
            .uri("$PROFILE_SERVICE_URL/me")
            .headers { headers ->
                headers.set("Authorization", tokenString)
            }
            .retrieve()
            .bodyToMono(Message.Success::class.java)
            .block()
            ?.let {
                val gson = Gson()
                return gson.fromJson(gson.toJson(it.data), UserModel::class.java)
            }
        return null
    }

    @Transactional
    fun createUser(userId: String, tokenString: String) : MessageUser {
        val userModel = getUserInformation(tokenString)
        val messageUser = MessageUser().let {
            it.email = userModel?.email ?: ""
            it.username = userModel?.username ?: ""
            it.phoneNumber = userModel?.phoneNumber ?: ""
            it.imageUrl = userModel?.media?.mediaUrl ?: ""
            it.externalUserId = userId
            it
        }
        return messageUserRepository.save(messageUser)
    }

    @Transactional
    fun saveUser(messageUser: MessageUser) : MessageUser {
        return messageUserRepository.save(messageUser)
    }

    fun getSessionUser(userId: String) : MessageUser? {
        return messageUserRepository.findByExternalUserId(userId)
    }
}