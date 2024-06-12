package com.example.backoffice.domain.product.dto

data class CreateProductRequestDto (
    val name: String,
    val price: Long,
    val description: String,
    val category: String,
    val stock: Int?,
    val discountRate: Int?,
    val imageUrl: String?
)