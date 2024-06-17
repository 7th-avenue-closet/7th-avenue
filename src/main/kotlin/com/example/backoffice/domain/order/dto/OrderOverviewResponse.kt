package com.example.backoffice.domain.order.dto

import com.example.backoffice.domain.order.model.Order
import com.example.backoffice.domain.order.model.OrderStatus
import java.time.ZonedDateTime

data class OrderOverviewResponse(
    val id: Long,
    val userId: Long,
    val totalAmount: Long,
    val status: OrderStatus,
    val createdAt: ZonedDateTime
) {
}

fun Order.toOverviewResponse(): OrderOverviewResponse {
    return OrderOverviewResponse(
        id = id!!,
        userId = user.id!!,
        totalAmount = totalPrice,
        status = status,
        createdAt = createdAt
    )
}
