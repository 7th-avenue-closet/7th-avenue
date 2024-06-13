package com.example.backoffice.domain.product.dto

import com.example.backoffice.domain.product.model.Product

data class IdResponseDto(
    val id: Long
)

fun Product.toIdResponse(): IdResponseDto {
    return IdResponseDto(id = id!!)
}