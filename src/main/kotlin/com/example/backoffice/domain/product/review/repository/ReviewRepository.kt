package com.example.backoffice.domain.product.review.repository

import com.example.backoffice.domain.product.review.model.Review
import org.springframework.data.jpa.repository.JpaRepository

interface ReviewRepository: JpaRepository<Review, Long> {
    fun findByIdAndDeletedAtIsNull(id: Long): Review?
    fun findAByDeletedAtIsNull(): List<Review>
}
