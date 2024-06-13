package com.example.backoffice.domain.product.review.controller

import com.example.backoffice.domain.product.review.dto.ReviewRequest
import com.example.backoffice.domain.product.review.service.ReviewService
import com.example.backoffice.infra.security.MemberPrincipal
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
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
        @AuthenticationPrincipal principal: MemberPrincipal,
        @PathVariable productId: Long,
        @RequestBody reviewRequest: ReviewRequest
    ): ResponseEntity<Unit> {
        reviewService.createReview(principal.id, productId, reviewRequest)
        return ResponseEntity.status(HttpStatus.OK).build()
    }

    @PutMapping("/{reviewId}")
    fun updateReview(
        @AuthenticationPrincipal principal: MemberPrincipal,
        @PathVariable productId: Long,
        @PathVariable reviewId: Long,
        @RequestBody reviewRequest: ReviewRequest
    ): ResponseEntity<Unit> {
        reviewService.updateReview(principal.id, productId, reviewId, reviewRequest)
        return ResponseEntity.status(HttpStatus.OK).build()
    }
}