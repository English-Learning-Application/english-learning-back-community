package com.security.app.controllers

import com.security.app.entities.MessageUser
import com.security.app.model.Message
import com.security.app.request.UpdateMessageUserRequest
import com.security.app.services.MessageUserService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/internal")
class MessageUserController(
    private val messageUserService: MessageUserService
) {
    @PostMapping("/message-users/update")
    fun updateMessageUser(
        @RequestBody request: UpdateMessageUserRequest
    ) : ResponseEntity<Message<MessageUser>> {
        try {
            val auth = SecurityContextHolder.getContext().authentication
            val userId = auth.name
            val messageUser = messageUserService.updateMessageUser(
                userId,
                request.username,
                request.imageUrl,
                request.email,
                request.phoneNumber
            )?: return ResponseEntity.badRequest().body(Message.Error("An error occurred", 400))
            return ResponseEntity.ok(Message.Success("User updated successfully", messageUser))
        }
        catch (e : Exception) {
            return ResponseEntity.badRequest().body(Message.Error(e.message ?: "An error occurred", 400))
        }
    }
}