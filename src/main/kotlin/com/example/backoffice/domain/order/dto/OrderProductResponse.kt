package com.example.backoffice.domain.order.dto

import com.example.backoffice.domain.order.model.OrderProduct

data class OrderProductResponse(
    val productId: Long,
    val quantity: Int,
    val price: Long
)

fun OrderProduct.toResponse(): OrderProductResponse {
    return OrderProductResponse(
        productId = product.id!!,
        quantity = quantity,
        price = price
    )
}