package com.example.backoffice.domain.order.model

import jakarta.persistence.Embeddable
import java.io.Serializable


@Embeddable
data class OrderProductPK(
    private val orderId: Long,
    private val productId: Long
) : Serializable {
    companion object {
        private const val serialVersionUID = 6003400618373609252L
    }
}
