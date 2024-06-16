package com.example.backoffice.domain.order.service

import com.example.backoffice.common.exception.ModelNotFoundException
import com.example.backoffice.domain.order.dto.OrderRequest
import com.example.backoffice.domain.order.model.Order
import com.example.backoffice.domain.order.model.OrderProduct
import com.example.backoffice.domain.order.model.OrderStatus
import com.example.backoffice.domain.order.repository.IOrderRepository
import com.example.backoffice.domain.order.repository.OrderProductRepository
import com.example.backoffice.domain.product.model.Product
import com.example.backoffice.domain.product.repository.ProductRepository
import com.example.backoffice.domain.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OrderService(
    private val iOrderRepository: IOrderRepository,
    private val orderProductRepository: OrderProductRepository,
    private val userRepository: UserRepository,
    private val productRepository: ProductRepository
) {
    @Transactional
    fun placeOrder(userId: Long, orderRequests: List<OrderRequest>) {
        val ids = orderRequests.map {
            it.productId
        }
        val productMap = productRepository.findByIds(ids).associateBy { it.id }
        checkOrderStock(productMap, orderRequests)

        val user = userRepository.findById(userId).orElseThrow()
        val orderProducts = orderRequests.map {
            OrderProduct.of(it.quantity, productMap[it.productId]!!.price, productMap[it.productId]!!)
        }
        val order = Order.of(
            totalPrice = calculateTotalPriceFromProducts(productMap, orderRequests).sum(),
            status = OrderStatus.PLACED,
            user = user,
            orderProducts = orderProducts
        )
        iOrderRepository.save(order)
        orderProductRepository.saveAll(orderProducts)

        orderRequests.forEach {
            val product = productMap[it.productId]!!
            product.stock -= it.quantity
        }
    }


    fun calculateTotalPriceFromProducts(
        productMap: Map<Long?, Product>,
        orderRequests: List<OrderRequest>
    ): List<Long> {
        val totalPrice = mutableListOf<Long>()
        orderRequests.forEach {
            val product = productMap[it.productId]!!
            totalPrice.add(product.price / 100 * (100 - product.discountRate) * it.quantity)
        }
        return totalPrice
    }

    fun checkOrderStock(productMap: Map<Long?, Product>, orderRequests: List<OrderRequest>) {
        orderRequests.forEach {
            val product = productMap[it.productId]!!
            if (it.quantity > product.stock) {
                throw IllegalArgumentException("unable to order more than stock")
            }
        }
    }

}
