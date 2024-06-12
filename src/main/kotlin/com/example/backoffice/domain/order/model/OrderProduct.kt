package com.example.backoffice.domain.order.model

import com.example.backoffice.domain.product.model.Product


class OrderProduct(
    val quantity: Int,
    val price: Long,
    val order: Order,
    val product: Product
) {
}