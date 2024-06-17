package com.example.backoffice.domain.product.repository

import com.example.backoffice.domain.product.model.Product
import org.springframework.data.jpa.repository.JpaRepository

interface ProductRepository : JpaRepository<Product, Long>, CustomProductRepository {
    fun findByIdAndDeletedAtIsNull(id: Long): Product?
    fun existsByIdAndDeletedAtIsNull(id: Long): Boolean
}