package com.security.app.request

data class UpdateMessageUserRequest(
    val username: String,
    val imageUrl: String,
    val email: String,
    val phoneNumber: String
)