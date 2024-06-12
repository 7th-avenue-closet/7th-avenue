package com.example.backoffice.domain.product.review.model

import java.io.InvalidObjectException

enum class Rating {
    ONE,
    TWO,
    THREE,
    FOUR,
    FIVE;

    companion object {
        fun fromString(rating: String): Rating {
            return try {
                valueOf(rating.uppercase())
            } catch (e: IllegalArgumentException) {
                throw InvalidObjectException("유효하지 않은 평점입니다, ONE부터 FIVE까지 하나를 입력해주세요!")
            }
        }
    }
}