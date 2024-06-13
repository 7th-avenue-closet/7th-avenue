package com.example.backoffice.domain.user.controller

import com.example.backoffice.domain.user.dto.LoginRequest
import com.example.backoffice.domain.user.dto.LoginResponse
import com.example.backoffice.domain.user.dto.SignUpRequest
import com.example.backoffice.domain.user.dto.SignUpResponse
import com.example.backoffice.domain.user.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController


@RestController
class UserController(private val userService: UserService) {
    @PostMapping("/auth/sign-up")
    fun signUp(@RequestBody signUpRequest: SignUpRequest): ResponseEntity<SignUpResponse> {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.signUp(signUpRequest))
    }

    @PostMapping("/auth/login")
    fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<LoginResponse> {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.login(loginRequest))
    }
}