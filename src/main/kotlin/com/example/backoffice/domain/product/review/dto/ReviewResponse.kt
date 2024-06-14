package com.example.backoffice.domain.product.review.dto

import com.example.backoffice.domain.product.dto.toResponse
import com.example.backoffice.domain.product.review.model.Rating
import com.example.backoffice.domain.product.review.model.Review
import java.time.ZonedDateTime

data class ReviewResponse(
    val id: Long,
    val rating: Rating,
    val comment: String,
    val createdAt: ZonedDateTime,
    val updatedAt: ZonedDateTime,
    val deletedAt: ZonedDateTime?,
    val productId : Long
    // val user: UserResponse
)
fun Review.toResponse(): ReviewResponse {
    return ReviewResponse(
        id = id!!,
        rating = rating,
        comment = comment,
        createdAt = createdAt,
        updatedAt = updatedAt,
        deletedAt = deletedAt,
        productId = product.toResponse().id
    )
}