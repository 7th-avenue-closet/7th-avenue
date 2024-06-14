package com.example.backoffice.domain.user.service

import com.example.backoffice.domain.user.model.PasswordHistory
import com.example.backoffice.domain.user.model.User
import com.example.backoffice.domain.user.repository.PasswordHistoryRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PasswordHistoryService(
    val passwordHistoryRepository: PasswordHistoryRepository,
    val passwordEncoder: PasswordEncoder,
) {
    fun checkPasswordNotInRecentHistory(user: User, newPassword: String) {
        if (passwordHistoryRepository.findTop3ByUserIdOrderByCreatedAtDesc(user.id!!)
                .any { passwordEncoder.matches(newPassword, it.password) }
        ) {
            throw IllegalArgumentException("New password cannot be the same as any of the last 3 passwords.")
        }
    }

    @Transactional
    fun updatePasswordHistory(user: User) {
        passwordHistoryRepository.save(PasswordHistory.of(user))
        val recentPasswords = passwordHistoryRepository.findByUserIdOrderByCreatedAtDesc(user.id!!)

        if (recentPasswords.size > 3) {
            val idsToDelete = recentPasswords.drop(3).map { it.id!! }
            passwordHistoryRepository.deleteAllById(idsToDelete)
        }
    }
}