package com.example.backoffice.domain.order.dto

data class OrderRequest(
    val productId: Long,
    val quantity: Int
)
