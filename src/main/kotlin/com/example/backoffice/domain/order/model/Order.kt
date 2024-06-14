package com.example.backoffice.domain.order.model

import com.example.backoffice.domain.user.model.User
import jakarta.persistence.*
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

@Entity
@Table(name = "app_order")
class Order(
    val totalPrice: Long,
    val status: String,
    val createdAt: ZonedDateTime,
    var updatedAt: ZonedDateTime,
    @ManyToOne
    val user: User
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    companion object {
        private fun validateOrder() {
            return
        }

        fun of(totalPrice: Long, status: String, user: User): Order {
            validateOrder()
            val timestamp = ZonedDateTime.now(ZoneId.of("Asia/Seoul"))
            return Order(
                totalPrice = totalPrice,
                status = status,
                user = user,
                createdAt = timestamp,
                updatedAt = timestamp
            )
        }
    }

}