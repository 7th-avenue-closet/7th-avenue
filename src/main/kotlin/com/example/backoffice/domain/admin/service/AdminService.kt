package com.example.backoffice.domain.admin.service

import com.example.backoffice.domain.admin.dto.AdminSignUpRequest
import com.example.backoffice.domain.admin.dto.AdminSignUpResponse
import com.example.backoffice.domain.admin.model.Admin
import com.example.backoffice.domain.admin.repository.AdminRepository
import com.example.backoffice.domain.user.dto.LoginRequest
import com.example.backoffice.domain.user.dto.LoginResponse
import com.example.backoffice.infra.security.MemberRole
import com.example.backoffice.infra.security.jwt.JwtHelper
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AdminService(
    private val adminRepository: AdminRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtHelper: JwtHelper,
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
}
