package com.example.backoffice.domain.product.model

import com.example.backoffice.domain.product.dto.CreateProductRequestDto
import com.example.backoffice.domain.product.dto.ProductDetailResponseDto
import com.example.backoffice.domain.product.dto.ProductResponseDto
import jakarta.persistence.*
import java.time.LocalDateTime
import java.time.ZonedDateTime

@Entity
@Table(name = "product")
class Product(
    @Column(name = "name", nullable = false)
    var name: String,

    @Column(name = "price", nullable = false)
    var price: Long,

    @Column(name = "description", nullable = false)
    var description: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    var category: Category,

    @Column(name = "stock")
    var stock: Int,

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    var status: Status,

    @Column(name = "discount_rate")
    var discountRate: Int?,

    @Column(name = "image_url")
    var imageUrl: String?,

    @Column(name = "created_at", nullable = false)
    val createdAt: ZonedDateTime,

    @Column(name = "updated_at", nullable = false)
    var updatedAt: ZonedDateTime,

    @Column(name = "is_deleted")
    var isDeleted: Boolean = false
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    fun status(stock: Int, discountRate: Int?) : Status {
        return if(stock < 1)  Status.SOLD_OUT
         else if(discountRate!!>0) Status.ON_SALE
         else Status.ON_DISCOUNT
    }

//    fun category(category: String) : Category {
//        return when (category) {
//            Category.ACC.name -> Category.ACC
//            Category.TOP.name -> Category.TOP
//            Category.OUTER.name -> Category.OUTER
//            Category.BOTTOM.name -> Category.BOTTOM
//            else -> throw IllegalArgumentException("Unknown category")
//        }
//    }
}

fun Product.toResponse() : ProductResponseDto{
    return ProductResponseDto(
        id = id!!,
        name = name,
        price = price,
        description = description,
        category = category,
        stock = stock,
        status = status,
        discountRate = discountRate,
        imageUrl = imageUrl,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
}

fun Product.toDetailResponse() : ProductDetailResponseDto{
    return ProductDetailResponseDto(
        id = id!!,
        name = name,
        price = price,
        description = description,
        category = category,
        stock = stock,
        status = status,
        discountRate = discountRate,
        imageUrl = imageUrl,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
}