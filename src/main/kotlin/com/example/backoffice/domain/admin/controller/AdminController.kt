package com.example.backoffice.domain.admin.controller

import com.example.backoffice.domain.admin.dto.AdminSignUpRequest
import com.example.backoffice.domain.admin.dto.AdminSignUpResponse
import com.example.backoffice.domain.admin.service.AdminService
import com.example.backoffice.domain.user.dto.LoginRequest
import com.example.backoffice.domain.user.dto.LoginResponse
import com.example.backoffice.infra.security.CustomPreAuthorize
import com.example.backoffice.infra.security.MemberPrincipal
import com.example.backoffice.infra.security.MemberRole
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RequestMapping("/admin")
@RestController
class AdminController(
    private val adminService: AdminService,
    private val preAuthorize: CustomPreAuthorize,

    ) {
    @PostMapping("/sign-up")
    fun adminSignUp(@RequestBody adminSignUpRequest: AdminSignUpRequest): ResponseEntity<AdminSignUpResponse> {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminService.createAdmin(adminSignUpRequest))
    }

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<LoginResponse> {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminService.login(loginRequest))
    }

    @DeleteMapping("/users/{userId}")
    fun deleteUser(
        @AuthenticationPrincipal principal: MemberPrincipal,
        @PathVariable userId: Long
    ): ResponseEntity<Unit> = preAuthorize.hasAnyRole(principal, setOf(MemberRole.ADMIN)) {
        adminService.deleteUser(userId)
        ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }
}
