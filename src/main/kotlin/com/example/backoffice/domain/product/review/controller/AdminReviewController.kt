package com.example.backoffice.domain.product.review.controller

import com.example.backoffice.domain.product.review.dto.ReviewResponse
import com.example.backoffice.domain.product.review.service.ReviewService
import com.example.backoffice.infra.security.CustomPreAuthorize
import com.example.backoffice.infra.security.MemberPrincipal
import com.example.backoffice.infra.security.MemberRole
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RequestMapping("/admin/reviews")
@RestController
class AdminReviewController(
    val reviewService: ReviewService,
    val preAuthorize: CustomPreAuthorize,
) {
    @GetMapping
    fun getReviews(
        @AuthenticationPrincipal principal: MemberPrincipal,
        @RequestParam userId: Long?
    ): ResponseEntity<List<ReviewResponse>> = preAuthorize.hasAnyRole(principal, setOf(MemberRole.ADMIN)) {
        ResponseEntity.status(HttpStatus.OK).body(reviewService.getReviews(userId))
    }

    @DeleteMapping
    fun deleteReviews(
        @AuthenticationPrincipal principal: MemberPrincipal,
        @RequestBody reviewIds: List<Long>
    ): ResponseEntity<Unit> = preAuthorize.hasAnyRole(principal, setOf(MemberRole.ADMIN)) {
        reviewService.deleteReviewsByAdmin(reviewIds)
        ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }
}