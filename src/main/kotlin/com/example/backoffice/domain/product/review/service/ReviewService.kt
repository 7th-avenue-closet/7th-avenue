package com.example.backoffice.domain.product.review.service

import com.example.backoffice.common.exception.ModelNotFoundException
import com.example.backoffice.common.exception.UnauthorizedException
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
    val reviewRepository: ReviewRepository, val productRepository: ProductRepository, val userRepository: UserRepository
) {
    @Transactional
    fun createReview(userId: Long, productId: Long, request: ReviewRequest) {
        val product = productRepository.findByIdOrNull(productId) ?: throw ModelNotFoundException("Product", productId)
        val (comment, rating) = request
        val reviewer = userRepository.findByIdOrNull(userId) ?: throw ModelNotFoundException("User", userId)

        val review = Review.of(
            comment = comment, rating = Rating.fromString(rating), user = reviewer, product = product
        )
        reviewRepository.save(review)
    }

    @Transactional
    fun updateReview(userId: Long, productId: Long, reviewId: Long, request: ReviewRequest) {
        if (!productRepository.existsById(productId)) throw ModelNotFoundException("Product", productId)
        val review = reviewRepository.findByIdOrNull(reviewId) ?: throw ModelNotFoundException("Review", reviewId)

        if (review.user.id != userId) throw UnauthorizedException("You do not have permission to modify.")

        val (comment, rating) = request

        review.updateReview(
            comment = comment, rating = rating
        )
    }


}