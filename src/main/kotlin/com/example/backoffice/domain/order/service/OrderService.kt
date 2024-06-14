package com.example.backoffice.domain.order.service

import com.example.backoffice.domain.order.dto.OrderRequest
import com.example.backoffice.domain.order.model.Order
import com.example.backoffice.domain.order.model.OrderProduct
import com.example.backoffice.domain.order.repository.IOrderRepository
import com.example.backoffice.domain.order.repository.OrderRepositoryImpl
import com.example.backoffice.domain.product.model.Product
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OrderService(
    private val iOrderRepository: IOrderRepository
) {
    @Transactional
    fun placeOrder(userId: Long, orderRequests: List<OrderRequest>) {
        val products = getProductsFromOrderRequest(orderRequests)
        val user = iOrderRepository.findUser(userId)
        val totalPrices = getTotalPriceFromProducts(products, orderRequests)
        val order = Order.of(
            totalPrice = totalPrices.sum(),
            status = "ON_SALE",
            user = user
        )
        iOrderRepository.save(order)
        val orderProducts = createOrderProducts(orderRequests, order, totalPrices)
        iOrderRepository.saveOrderProducts(orderProducts) // 실제로 저장 쿼리가 하나만 나가는지 확인 필요
    }


    fun getProductsFromOrderRequest(orderRequests: List<OrderRequest>): List<Product> {
        val ids = mutableListOf<Long>()
        for (orderRequest in orderRequests) {
            ids.add(orderRequest.productId)
        }
        return iOrderRepository.findProductsByIds(ids)
    }

    fun getTotalPriceFromProducts(products: List<Product>, orderRequests: List<OrderRequest>): List<Long> {
        val totalPrice = mutableListOf<Long>()
        for ((index, product) in products.withIndex()) {
            totalPrice.add(product.price * orderRequests[index].quantity)
        }
        return totalPrice
    }

    fun createOrderProducts(
        orderRequests: List<OrderRequest>,
        order: Order,
        totalPrices: List<Long>
    ): List<OrderProduct> {
        val orderProducts = mutableListOf<OrderProduct>()
        for ((index, orderRequest) in orderRequests.withIndex()) {
            orderProducts.add(
                OrderProduct.of(
                    orderId = order.id!!,
                    productId = orderRequest.productId,
                    quantity = orderRequest.quantity,
                    price = totalPrices[index]
                )
            )
        }
        return orderProducts
    }

}
