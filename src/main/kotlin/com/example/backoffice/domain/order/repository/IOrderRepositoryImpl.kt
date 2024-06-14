package com.example.backoffice.domain.order.repository

import com.example.backoffice.domain.order.model.Order
import com.example.backoffice.domain.order.model.OrderProduct
import com.example.backoffice.domain.product.model.Product
import com.example.backoffice.domain.product.repository.ProductRepository
import com.example.backoffice.domain.user.model.User
import com.example.backoffice.domain.user.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class IOrderRepositoryImpl(
    private val orderRepository: OrderRepository,
    private val orderProductRepository: OrderProductRepository,
    private val productRepository: ProductRepository,
    private val userRepository: UserRepository
) : IOrderRepository {

    override fun findUser(userId: Long): User {
        return userRepository.findById(userId).orElseThrow()
    }

    override fun save(order: Order) {
        orderRepository.save(order)
    }

    override fun findProductsByIds(ids: List<Long>): List<Product> {
        return productRepository.findByIds(ids)
    }

    override fun saveOrderProducts(orderProducts: List<OrderProduct>) {
        orderProductRepository.saveAll(orderProducts)
    }

}