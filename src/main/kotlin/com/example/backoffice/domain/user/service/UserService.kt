package com.example.backoffice.domain.user.service

import com.example.backoffice.common.exception.ModelNotFoundException
import com.example.backoffice.domain.user.dto.*
import com.example.backoffice.domain.user.model.User
import com.example.backoffice.domain.user.repository.UserRepository
import com.example.backoffice.infra.redis.RedisTokenRepository
import com.example.backoffice.infra.security.MemberRole
import com.example.backoffice.infra.security.jwt.JwtHelper
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
    private val jwtHelper: JwtHelper,
    private val passwordEncoder: PasswordEncoder,
    private val passwordHistoryService: PasswordHistoryService,
    private val redisTokenRepository: RedisTokenRepository,
) {
    @Transactional
    fun signUp(request: SignUpRequest): SignUpResponse {
        val (accountId, password, name, imageUrl) = request
        if (userRepository.existsByAccountId(accountId)) {
            throw IllegalArgumentException("Account id: '${accountId}' already exists.")
        }
        checkPasswordRule(password)

        val encodedPassword = passwordEncoder.encode(password)
        val user = User.of(
            accountId = accountId,
            name = name,
            password = encodedPassword,
            imageUrl = imageUrl
        )
        return userRepository.save(user)
            .also { passwordHistoryService.updatePasswordHistory(it) }
            .run { SignUpResponse(userId = id!!) }
    }

    fun login(request: LoginRequest): LoginResponse {
        val user = userRepository.findByAccountId(request.accountId)
            ?.takeIf { passwordEncoder.matches(request.password, it.password) }
            ?: throw IllegalArgumentException("Invalid Credential.")

        return LoginResponse(accessToken = jwtHelper.generateToken(user.id!!, MemberRole.USER))
    }

    @Transactional
    fun updatePassword(userId: Long, request: UpdatePasswordRequest) {
        val user = userRepository.findByIdAndDeletedAtIsNull(userId) ?: throw ModelNotFoundException("User", userId)
        val (currentPassword, newPassword) = request

        if (!passwordEncoder.matches(currentPassword, user.password)) {
            throw IllegalArgumentException("Invalid Credential.")
        }

        checkPasswordRule(newPassword)
        passwordHistoryService.checkPasswordNotInRecentHistory(user, newPassword)

        user.updatePassword(newPassword = passwordEncoder.encode(newPassword))
        passwordHistoryService.updatePasswordHistory(user)
    }

    @Transactional
    fun updateProfile(userId: Long, request: UpdateProfileRequest) {
        val user = userRepository.findByIdOrNull(userId) ?: throw ModelNotFoundException("User", userId)

        user.updateProfile(request.name, request.imageUrl)
    }

    fun logout(token: String) {
        redisTokenRepository.blacklistToken(token)
    }

    fun isLoggedOut(token: String): Boolean {
        return redisTokenRepository.isTokenBlacklisted(token)
    }

    private fun checkPasswordRule(password: String) {
        if (!password.matches("^[a-zA-Z0-9!@#\$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]{8,15}\$".toRegex())) {
            throw IllegalArgumentException("Invalid Password.")
        }
    }
}

