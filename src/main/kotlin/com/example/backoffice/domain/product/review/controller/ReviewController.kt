package com.example.backoffice.domain.product.review.controller

import com.example.backoffice.domain.product.review.dto.CreateReviewRequest
import com.example.backoffice.domain.product.review.service.ReviewService
import com.example.backoffice.domain.user.model.User
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/products/{productId}/reviews")
@RestController
class ReviewController(
    val reviewService: ReviewService
) {
    @PostMapping
    fun createReview(
        @AuthenticationPrincipal user: User,
        @PathVariable productId: Long,
        @RequestBody createReviewRequest: CreateReviewRequest
    ): ResponseEntity<Unit> {
        reviewService.createReview(user, productId, createReviewRequest)
        return ResponseEntity
            .status(HttpStatus.OK)
            .build()
    }
}