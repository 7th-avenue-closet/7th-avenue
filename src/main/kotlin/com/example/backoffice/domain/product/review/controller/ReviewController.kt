package com.example.backoffice.domain.product.review.controller

import com.example.backoffice.domain.product.review.dto.ReviewRequest
import com.example.backoffice.domain.product.review.service.ReviewService
import com.example.backoffice.infra.security.CustomPreAuthorize
import com.example.backoffice.infra.security.MemberPrincipal
import com.example.backoffice.infra.security.MemberRole
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RequestMapping("/products/{productId}/reviews")
@RestController
class ReviewController(
    val reviewService: ReviewService,
    val preAuthorize: CustomPreAuthorize,
) {
    @PostMapping
    fun createReview(
        @AuthenticationPrincipal principal: MemberPrincipal,
        @PathVariable productId: Long,
        @RequestBody reviewRequest: ReviewRequest,
    ): ResponseEntity<Unit> = preAuthorize.hasAnyRole(principal, setOf(MemberRole.USER)) {
        reviewService.createReview(principal.id, productId, reviewRequest)
        ResponseEntity.status(HttpStatus.OK).build()
    }

    @PutMapping("/{reviewId}")
    fun updateReview(
        @AuthenticationPrincipal principal: MemberPrincipal,
        @PathVariable productId: Long,
        @PathVariable reviewId: Long,
        @RequestBody reviewRequest: ReviewRequest,
    ): ResponseEntity<Unit> = preAuthorize.hasAnyRole(principal, setOf(MemberRole.USER, MemberRole.ADMIN)) {
        reviewService.updateReview(principal.id, productId, reviewId, reviewRequest)
        ResponseEntity.status(HttpStatus.OK).build()
    }

    @DeleteMapping("/{reviewId}")
    fun deleteReview(
        @AuthenticationPrincipal principal: MemberPrincipal,
        @PathVariable productId: Long,
        @PathVariable reviewId: Long,
    ): ResponseEntity<Unit> = preAuthorize.hasAnyRole(principal, setOf(MemberRole.USER, MemberRole.ADMIN)) {
        reviewService.deleteReview(principal, productId, reviewId)
        ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }
}