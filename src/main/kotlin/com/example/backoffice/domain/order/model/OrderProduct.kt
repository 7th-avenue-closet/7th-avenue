package com.example.backoffice.domain.order.model

import com.example.backoffice.domain.product.model.Product
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity

@Entity
class OrderProduct(
    val quantity: Int,
    val price: Long,

    @EmbeddedId
    val id: OrderProductPK
) {

    companion object {
        private fun validateOrderProduct() {
            return
        }

        fun of(quantity: Int, price: Long, orderId: Long, productId: Long): OrderProduct {
            validateOrderProduct()
            return OrderProduct(
                quantity = quantity,
                price = price,
                id = OrderProductPK(orderId, productId)
            )
        }
    }
}