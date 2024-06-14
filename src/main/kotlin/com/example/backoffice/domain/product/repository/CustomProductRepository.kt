package com.example.backoffice.domain.product.repository

import com.example.backoffice.domain.product.model.Category
import com.example.backoffice.domain.product.model.Product
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface CustomProductRepository {
    fun findByPageableAndDeleted(pageSize: Long, sorted: String?, cursor: Long?, category: Category?): List<Product>
}