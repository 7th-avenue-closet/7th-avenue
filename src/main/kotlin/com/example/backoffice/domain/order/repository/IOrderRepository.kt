package com.example.backoffice.domain.order.repository

import com.example.backoffice.domain.order.model.Order
import com.example.backoffice.domain.order.model.OrderProduct
import com.example.backoffice.domain.product.model.Product
import com.example.backoffice.domain.user.model.User
import org.springframework.stereotype.Repository

import java.util.*

interface IOrderRepository {
    fun save(order: Order)
    fun findById(orderId: Long): Optional<Order>
    fun findAllByUserId(userId: Long): List<Order>

}