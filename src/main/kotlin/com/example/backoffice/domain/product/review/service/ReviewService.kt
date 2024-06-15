package com.example.backoffice.domain.product.review.service

import com.example.backoffice.common.exception.AccessDeniedException
import com.example.backoffice.common.exception.ModelNotFoundException
import com.example.backoffice.domain.product.repository.ProductRepository
import com.example.backoffice.domain.product.review.dto.ReviewRequest
import com.example.backoffice.domain.product.review.model.Rating
import com.example.backoffice.domain.product.review.model.Review
import com.example.backoffice.domain.product.review.repository.ReviewRepository
import com.example.backoffice.domain.user.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ReviewService(
    val reviewRepository: ReviewRepository,
    val productRepository: ProductRepository,
    val userRepository: UserRepository,
) {
    @Transactional
    fun createReview(userId: Long, productId: Long, request: ReviewRequest) {
        val product = productRepository.findByIdAndDeletedAtIsNull(productId) ?: throw ModelNotFoundException(
            "Product",
            productId
        )
        val (comment, rating, imageUrl) = request
        val reviewer = userRepository.findByIdOrNull(userId) ?: throw ModelNotFoundException("User", userId)

        val review = Review.of(
            comment = comment, rating = Rating.fromString(rating), imageUrl = imageUrl, user = reviewer, product = product
        )
        reviewRepository.save(review)
    }

    @Transactional
    fun updateReview(userId: Long, productId: Long, reviewId: Long, request: ReviewRequest) {
        if (!productRepository.existsByIdAndDeletedAtIsNull(productId)) throw ModelNotFoundException(
            "Product",
            productId
        )
        val review =
            reviewRepository.findByIdAndDeletedAtIsNull(reviewId) ?: throw ModelNotFoundException("Review", reviewId)

        if (review.user.id != userId) throw AccessDeniedException("You do not have permission to modify.")

        val (comment, rating, imageUrl) = request

        review.updateReview(
            comment = comment, rating = rating, imageUrl = imageUrl
        )
    }

    @Transactional
    fun deleteReview(userId: Long, productId: Long, reviewId: Long) {
        if (!productRepository.existsByIdAndDeletedAtIsNull(productId)) throw ModelNotFoundException(
            "Product",
            productId
        )
        val review =
            reviewRepository.findByIdAndDeletedAtIsNull(reviewId) ?: throw ModelNotFoundException("Review", reviewId)

        if (review.user.id != userId) throw AccessDeniedException("You do not have permission to delete.")

        review.softDelete()
    }
}
