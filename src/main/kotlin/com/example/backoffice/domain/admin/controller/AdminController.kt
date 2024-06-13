package com.example.backoffice.domain.admin.controller

import com.example.backoffice.domain.admin.dto.CreateAdminRequest
import com.example.backoffice.domain.admin.dto.CreateAdminResponse
import com.example.backoffice.domain.admin.service.AdminService
import com.example.backoffice.domain.user.dto.LoginRequest
import com.example.backoffice.domain.user.dto.LoginResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/admin")
@RestController
class AdminController(private val adminService: AdminService) {
    @PostMapping
    fun createAdmin(@RequestBody createAdminRequest: CreateAdminRequest): ResponseEntity<CreateAdminResponse> {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminService.createAdmin(createAdminRequest))
    }

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<LoginResponse> {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminService.login(loginRequest))
    }
}