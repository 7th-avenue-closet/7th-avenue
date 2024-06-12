package com.example.backoffice.domain.product.review.service

import com.example.backoffice.domain.product.repository.ProductRepository
import com.example.backoffice.domain.product.review.dto.CreateReviewRequest
import com.example.backoffice.domain.product.review.model.Rating
import com.example.backoffice.domain.product.review.model.Review
import com.example.backoffice.domain.product.review.repository.ReviewRepository
import com.example.backoffice.domain.user.model.User
import com.example.backoffice.domain.user.repository.UserRepository
import com.example.backoffice.exception.ModelNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ReviewService(
    val reviewRepository: ReviewRepository,
    val productRepository: ProductRepository,
    val userRepository: UserRepository
) {
    @Transactional
    fun createReview(user: User, productId: Long, request: CreateReviewRequest) {
        val product = productRepository.findByIdOrNull(productId) ?: throw ModelNotFoundException("Product", productId)
        val (comment) = request
        val userId = user.id
        val user = userRepository.findByIdOrNull(userId)
            ?: throw ModelNotFoundException("User", userId)
        val rating = Rating.fromString(request.rating)

        val review = Review.of(
            comment = comment,
            rating = rating,
            user = user,
            product = product
        )
        reviewRepository.save(review)
    }
}
