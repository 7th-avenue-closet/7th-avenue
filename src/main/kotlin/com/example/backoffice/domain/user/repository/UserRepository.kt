package com.example.backoffice.domain.user.repository

import com.example.backoffice.domain.user.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<User, Long> {
}
