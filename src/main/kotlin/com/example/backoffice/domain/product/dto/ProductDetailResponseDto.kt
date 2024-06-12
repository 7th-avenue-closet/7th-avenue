package com.example.backoffice.domain.product.dto

import com.example.backoffice.domain.product.model.Category
import com.example.backoffice.domain.product.model.Status
import com.example.backoffice.domain.product.review.model.Review

data class ProductDetailResponseDto (
    val id: Long,
    val name: String,
    val price: Long,
    val description: String,
    val category: Category,
    val status: Status,
    val stock: Int?,
    val discountRate: Int?,
    val imageUrl: String?,
//    val review: ReviewResponse
)