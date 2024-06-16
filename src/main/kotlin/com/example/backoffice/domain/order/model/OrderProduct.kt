package com.example.backoffice.domain.order.model

import com.example.backoffice.domain.product.model.Product
import jakarta.persistence.*

@Entity
class OrderProduct(
    val quantity: Int,
    val price: Long,
    @ManyToOne
    val product: Product
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    companion object {
        private fun validateOrderProduct() {
            return
        }

        fun of(quantity: Int, price: Long, product: Product): OrderProduct {
            validateOrderProduct()
            return OrderProduct(
                quantity = quantity,
                price = price,
                product = product
            )
        }
    }
}