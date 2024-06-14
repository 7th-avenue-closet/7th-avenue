package com.example.backoffice.domain.user.repository

import com.example.backoffice.domain.user.model.PasswordHistory
import org.springframework.data.jpa.repository.JpaRepository

interface PasswordHistoryRepository : JpaRepository<PasswordHistory, Long> {
    fun findTop3ByUserIdOrderByCreatedAtDesc(userId: Long): List<PasswordHistory>
    fun findByUserIdOrderByCreatedAtDesc(userId: Long): List<PasswordHistory>
}