package com.example.backoffice.domain.user.model

import jakarta.persistence.*

@Entity
@Table(name = "app_user")
class User(
    @Column(name = "account_id", nullable = false, unique = true)
    val accountId: String,

    @Column(name = "name", nullable = false)
    var name: String,

    @Column(name = "password", nullable = false)
    var password: String,

    @Column(name = "image_url")
    var imageUrl: String?,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    fun updatePassword(newPassword: String) {
        this.password = newPassword
    }

    companion object {
        private fun checkAccountId(newAccountId: String) {
            if (newAccountId.matches("^[a-z0-9]{4,10}$".toRegex()) == false) {
                throw IllegalArgumentException("Invalid account ID.")
            }
        }

        fun of(
            accountId: String,
            name: String,
            password: String,
            imageUrl: String?,
        ): User {
            checkAccountId(accountId)
            return User(
                accountId = accountId,
                name = name,
                password = password,
                imageUrl = imageUrl
            )
        }
    }
}