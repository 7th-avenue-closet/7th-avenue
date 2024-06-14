package com.example.backoffice.domain.order.repository

import com.example.backoffice.domain.order.model.OrderProduct
import com.example.backoffice.domain.order.model.OrderProductPK
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderProductRepository : JpaRepository<OrderProduct, OrderProductPK> {
}