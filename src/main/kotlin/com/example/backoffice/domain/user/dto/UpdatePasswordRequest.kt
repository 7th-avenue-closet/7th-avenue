package com.example.backoffice.domain.user.dto

data class UpdatePasswordRequest(
    val currentPassword: String,
    val newPassword: String,
)
