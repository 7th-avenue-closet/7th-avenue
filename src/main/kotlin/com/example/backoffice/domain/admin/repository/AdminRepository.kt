package com.example.backoffice.domain.admin.repository

import com.example.backoffice.domain.admin.model.Admin
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AdminRepository : JpaRepository<Admin, Long> {
    fun existsByAccountId(accountId: String): Boolean
    fun findByAccountId(accountId: String): Admin?
}
