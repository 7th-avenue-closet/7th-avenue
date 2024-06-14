package com.example.backoffice.domain.admin.service

import com.example.backoffice.common.exception.AlreadyDeletedException
import com.example.backoffice.domain.admin.dto.AdminSignUpRequest
import com.example.backoffice.domain.admin.dto.AdminSignUpResponse
import com.example.backoffice.domain.admin.model.Admin
import com.example.backoffice.domain.admin.repository.AdminRepository
import com.example.backoffice.domain.product.review.dto.ReviewResponse
import com.example.backoffice.domain.product.review.dto.toResponse
import com.example.backoffice.domain.product.review.repository.ReviewRepository
import com.example.backoffice.domain.user.dto.LoginRequest
import com.example.backoffice.domain.user.dto.LoginResponse
import com.example.backoffice.infra.security.MemberRole
import com.example.backoffice.infra.security.jwt.JwtHelper
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.ZonedDateTime

@Service
class AdminService(
    private val adminRepository: AdminRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtHelper: JwtHelper,
    private val reviewRepository: ReviewRepository,
) {
    fun createAdmin(request: AdminSignUpRequest): AdminSignUpResponse {
        val (accountId, password) = request
        if (adminRepository.existsByAccountId(accountId)) {
            throw IllegalArgumentException("Admin with account id $accountId already exists")
        }

        return adminRepository.save(Admin.of(accountId, passwordEncoder.encode(password)))
            .run { AdminSignUpResponse(id = id!!) }
    }

    fun login(request: LoginRequest): LoginResponse {
        val admin = adminRepository.findByAccountId(request.accountId)
            ?.takeIf { passwordEncoder.matches(request.password, it.password) }
            ?: throw IllegalArgumentException("Invalid Credential.")

        return LoginResponse(accessToken = jwtHelper.generateToken(admin.id!!, MemberRole.ADMIN))
    }

    fun getAllReviews(): List<ReviewResponse> {
        return reviewRepository.findAByDeletedAtIsNull().map { it.toResponse() }
    }

    @Transactional
    fun deleteReviews(reviewIds: List<Long>) {
        val reviews = reviewRepository.findAllById(reviewIds)
        val now = ZonedDateTime.now()

        reviews.forEach {
            if (it.deletedAt != null) {
                throw AlreadyDeletedException("Review", it.id!!)
            }
            it.deletedAt = now
        }
        reviewRepository.saveAll(reviews)
    }
}
