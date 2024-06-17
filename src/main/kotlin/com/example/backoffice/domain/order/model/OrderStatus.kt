package com.example.backoffice.domain.order.model

import java.io.InvalidObjectException

enum class OrderStatus {
    PLACED,
    SHIPPING,
    CONFIRMED,
    CANCELLED;

    companion object {
        fun fromString(status: String): OrderStatus {
            return try {
                valueOf(status.uppercase())
            } catch (e: IllegalArgumentException) {
                throw InvalidObjectException("Invalid order status")
            }
        }
    }
}
