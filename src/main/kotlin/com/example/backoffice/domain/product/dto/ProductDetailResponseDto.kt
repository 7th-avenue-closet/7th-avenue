package com.example.backoffice.domain.product.dto

import com.example.backoffice.domain.product.model.Category
import com.example.backoffice.domain.product.model.Product
import com.example.backoffice.domain.product.model.Status
import com.example.backoffice.domain.product.review.model.Review
import java.time.ZonedDateTime

data class ProductDetailResponseDto(
    val id: Long,
    val name: String,
    val price: Long,
    val description: String,
    val category: Category,
    val status: Status,
    val stock: Int?,
    val discountRate: Int?,
    val imageUrl: String?,
    val createdAt: ZonedDateTime,
    val updatedAt: ZonedDateTime
//    val review: ReviewResponse
)


fun Product.toDetailResponse(): ProductDetailResponseDto {
    return ProductDetailResponseDto(
        id = id!!,
        name = name,
        price = price,
        description = description,
        category = category,
        stock = stock,
        status = status,
        discountRate = discountRate,
        imageUrl = imageUrl,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
}