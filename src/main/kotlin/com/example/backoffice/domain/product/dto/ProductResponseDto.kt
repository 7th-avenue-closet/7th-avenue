package com.example.backoffice.domain.product.dto

import com.example.backoffice.domain.product.model.Category
import com.example.backoffice.domain.product.model.Status
import java.time.ZonedDateTime

data class ProductResponseDto (
    val id: Long,
    val name: String,
    val price: Long,
    val description: String,
    val category: String,
    val status: String,
    val stock: Int?,
    val discountRate: Int?,
    val imageUrl: String?,
    val createdAt : ZonedDateTime,
    val updatedAt : ZonedDateTime
)