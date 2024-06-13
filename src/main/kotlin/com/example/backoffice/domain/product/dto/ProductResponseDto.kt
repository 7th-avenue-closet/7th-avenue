package com.example.backoffice.domain.product.dto

import com.example.backoffice.domain.product.model.Category
import com.example.backoffice.domain.product.model.Product
import com.example.backoffice.domain.product.model.Status
import java.time.ZonedDateTime

data class ProductResponseDto(
    val id: Long,
    val name: String,
    val price: Long,
    val description: String,
    val category: String,
    val status: String,
    val stock: Int?,
    val discountRate: Int?,
    val imageUrl: String?,
    val createdAt: ZonedDateTime,
    val updatedAt: ZonedDateTime
)

fun Product.toResponse(): ProductResponseDto {
    return ProductResponseDto(
        id = id!!,
        name = name,
        price = price,
        description = description,
        category = category.name,
        stock = stock,
        status = status.name,
        discountRate = discountRate,
        imageUrl = imageUrl,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
}