package com.example.backoffice.domain.product.repository

import com.example.backoffice.domain.product.model.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface ProductRepository : JpaRepository<Product, Long> {
    fun findAllByDeletedAtIsNull(): List<Product>
    fun findByIdAndDeletedAtIsNull(id: Long): Product?
    fun existsByIdAndDeletedAtIsNull(id: Long): Boolean

    @Query("SELECT p FROM Product p WHERE p.id IN :ids")
    fun findByIds(@Param("ids") ids: List<Long>): List<Product>
}