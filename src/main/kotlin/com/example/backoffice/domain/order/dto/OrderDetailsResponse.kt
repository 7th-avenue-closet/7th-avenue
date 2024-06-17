package com.example.backoffice.domain.order.dto

import com.example.backoffice.domain.order.model.Order
import com.example.backoffice.domain.order.model.OrderStatus
import java.time.ZonedDateTime

data class OrderDetailsResponse(
    val id: Long,
    val totalPrice: Long,
    val createdAt: ZonedDateTime,
    val userId: Long,
    val status: OrderStatus,
    val orderProducts: List<OrderProductResponse>
)

fun Order.toDetailResponse(): OrderDetailsResponse {
    return OrderDetailsResponse(
        id = id!!,
        totalPrice = totalPrice,
        createdAt = createdAt,
        userId = user.id!!,
        status = status,
        orderProducts = orderProducts.map { it.toResponse() }
    )
}
