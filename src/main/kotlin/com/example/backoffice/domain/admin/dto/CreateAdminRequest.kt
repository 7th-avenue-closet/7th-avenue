package com.example.backoffice.domain.admin.dto

data class CreateAdminRequest(
    val accountId: String,
    val password: String,
)
