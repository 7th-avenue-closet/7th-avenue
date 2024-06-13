package com.example.backoffice.domain.user.dto

data class SignUpRequest(
    val accountId: String,
    val password: String,
    val name: String,
    val imageUrl: String?,
)
