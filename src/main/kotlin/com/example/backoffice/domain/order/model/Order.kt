package com.example.backoffice.domain.order.model

import com.example.backoffice.domain.user.model.User
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "app_order")
class Order(
    val totalPrice: Long,
    val status: String,
    val createdAt: LocalDateTime,
    var updatedAt: LocalDateTime,
    @ManyToOne
    val user: User
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

}