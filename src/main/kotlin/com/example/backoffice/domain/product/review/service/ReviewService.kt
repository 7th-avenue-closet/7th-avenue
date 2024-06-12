package com.example.backoffice.domain.product.review.service

import com.example.backoffice.domain.product.review.dto.CreateReviewRequest
import com.example.backoffice.domain.product.review.repository.ReviewRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ReviewService(
    val reviewRepository: ReviewRepository
) {
    @Transactional
    fun createReview(productId: Long, createReviewRequest: CreateReviewRequest) {
        TODO()
    }
}
