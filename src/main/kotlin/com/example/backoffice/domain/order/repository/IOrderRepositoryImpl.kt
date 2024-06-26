package com.example.backoffice.domain.order.repository

import com.example.backoffice.domain.order.model.Order
import com.example.backoffice.domain.order.model.OrderProduct
import com.example.backoffice.domain.product.model.Product
import com.example.backoffice.domain.product.repository.ProductRepository
import com.example.backoffice.domain.user.model.User
import com.example.backoffice.domain.user.repository.UserRepository

import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import java.util.*

@Component
class IOrderRepositoryImpl(
    private val orderRepository: OrderRepository,
    private val orderProductRepository: OrderProductRepository
) : IOrderRepository {

    override fun save(order: Order) {
        orderRepository.save(order)
    }

    override fun findById(orderId: Long): Optional<Order> {
        return orderRepository.findById(orderId)
    }

    override fun findAllByUserId(userId: Long): List<Order> {
        return orderRepository.findAllByUserId(userId)
    }

    override fun findAll(): List<Order> {
        return orderRepository.findAll()
    }

}