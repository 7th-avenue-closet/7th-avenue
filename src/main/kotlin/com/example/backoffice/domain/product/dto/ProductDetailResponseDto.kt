package com.example.backoffice.domain.product.dto

import com.example.backoffice.domain.product.review.dto.ReviewResponse

data class ProductDetailResponseDto(
    val product: ProductResponseDto,
    val review: List<ReviewResponse>
)