package com.example.backoffice.domain.user.repository

import com.example.backoffice.domain.user.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun existsByAccountId(accountId: String): Boolean
    fun findByAccountId(accountId: String): User?
}
