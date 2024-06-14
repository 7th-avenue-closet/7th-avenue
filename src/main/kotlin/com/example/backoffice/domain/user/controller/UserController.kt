package com.example.backoffice.domain.user.controller

import com.example.backoffice.domain.user.dto.*
import com.example.backoffice.domain.user.service.UserService
import com.example.backoffice.infra.security.CustomPreAuthorize
import com.example.backoffice.infra.security.MemberPrincipal
import com.example.backoffice.infra.security.MemberRole
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController


@RestController
class UserController(private val userService: UserService, private val preAuthorize: CustomPreAuthorize) {
    @PostMapping("/auth/sign-up")
    fun signUp(@RequestBody signUpRequest: SignUpRequest): ResponseEntity<SignUpResponse> {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.signUp(signUpRequest))
    }

    @PostMapping("/auth/login")
    fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<LoginResponse> {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.login(loginRequest))
    }

    @PatchMapping("/auth/change-password")
    fun changePassword(
        @AuthenticationPrincipal principal: MemberPrincipal,
        updatePasswordRequest: UpdatePasswordRequest,
    ): ResponseEntity<Unit> = preAuthorize.hasAnyRole(principal, setOf(MemberRole.USER)) {
        userService.updatePassword(principal.id, updatePasswordRequest)
        ResponseEntity.status(HttpStatus.OK).build()
    }
}