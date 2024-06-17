package com.example.backoffice.domain.product.review.repository

import com.example.backoffice.domain.product.review.model.Review

interface CustomReviewRepository {
    fun getReviews(userId: Long?): List<Review>
    fun getReviews(cursor: Long?, productId: Long): List<Review>
}