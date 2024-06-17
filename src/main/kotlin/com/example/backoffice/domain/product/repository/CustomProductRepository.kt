package com.example.backoffice.domain.product.repository

import com.example.backoffice.domain.product.model.Category
import com.example.backoffice.domain.product.model.Product

interface CustomProductRepository {
    fun findByPageableAndDeleted(
        pageSize: Long,
        sorted: String,
        cursor: Any?,
        category: Category?,
        name: String?,
        onDiscount: Boolean?,
    ): List<Product>
}