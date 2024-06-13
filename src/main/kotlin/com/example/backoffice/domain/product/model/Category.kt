package com.example.backoffice.domain.product.model

import java.io.InvalidObjectException

enum class Category {
    OUTER,
    TOP,
    BOTTOM,
    ACC;

    companion object {
        fun fromString(category: String) : Category {
            return try {
                valueOf(category.uppercase())
            } catch (e: IllegalArgumentException) {
                throw InvalidObjectException("unknown category: $category.")
            }
        }
    }


}