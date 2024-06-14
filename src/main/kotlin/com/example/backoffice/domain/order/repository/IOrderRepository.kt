package com.example.backoffice.domain.order.repository

import com.example.backoffice.domain.order.model.Order
import com.example.backoffice.domain.order.model.OrderProduct
import com.example.backoffice.domain.product.model.Product
import com.example.backoffice.domain.user.model.User
import org.springframework.stereotype.Repository

interface IOrderRepository {
    fun findUser(userId: Long): User
    fun save(order: Order)
    fun findProductsByIds(ids: List<Long>): List<Product>
    fun saveOrderProducts(orderProducts: List<OrderProduct>)
    //fun find()
}