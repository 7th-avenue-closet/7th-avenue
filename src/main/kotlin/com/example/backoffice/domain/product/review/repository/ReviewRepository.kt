package com.example.backoffice.domain.product.review.repository

import com.example.backoffice.domain.product.review.model.Review
import org.springframework.data.jpa.repository.JpaRepository

interface ReviewRepository : JpaRepository<Review, Long>, CustomReviewRepository {
    fun findByIdAndDeletedAtIsNull(id: Long): Review?
    fun findByDeletedAtIsNull(): List<Review>
    fun findByUserIdAndDeletedAtIsNull(userId: Long): List<Review>
    fun findByIdInAndDeletedAtIsNull(ids: List<Long>): List<Review>
}
