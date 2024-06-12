package com.example.backoffice.domain.product.review.dto

data class CreateReviewRequest(
    val comment: String,
    val rating: String,
)
