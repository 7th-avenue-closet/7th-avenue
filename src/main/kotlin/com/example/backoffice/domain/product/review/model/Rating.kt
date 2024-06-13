package com.example.backoffice.domain.product.review.model

import java.io.InvalidObjectException

enum class Rating {
    ONE, TWO, THREE, FOUR, FIVE;

    companion object {
        fun fromString(rating: String): Rating {
            return try {
                valueOf(rating.uppercase())
            } catch (e: IllegalArgumentException) {
                throw InvalidObjectException("Invalid rating. Please enter a value between ONE and FIVE!")
            }
        }
    }
}