package com.example.backoffice.domain.product.review.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/products/{productId}/reviews")
@RestController
class ReviewController(
    val reviewService: ReviewService
) {
    fun createReview(
        @PathVariable productId: Long,
        @RequestBody createReviewRequest: CreateReviewReuqest
    ): ResponseEntity<ReviewResposne> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(reviewService.createReview(productId, createReviewRequest))
    }
}