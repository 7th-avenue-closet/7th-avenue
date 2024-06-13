package com.example.backoffice.domain.product.model

import com.example.backoffice.domain.product.dto.IdResponseDto
import com.example.backoffice.domain.product.dto.ProductDetailResponseDto
import com.example.backoffice.domain.product.dto.ProductResponseDto
import com.example.backoffice.domain.product.dto.UpdateProductRequestDto
import jakarta.persistence.*
import java.time.ZoneId
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

    @Column(name = "stock", nullable = false)
    var stock: Int,

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    var status: Status,

    @Column(name = "discount_rate", nullable = false)
    var discountRate: Int,

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


    fun update(request: UpdateProductRequestDto) {
        name = request.name
        price = request.price
        description = request.description
        category = Category.fromString(request.category)
        stock = request.stock
        status = Status.calc(stock, discountRate)
        imageUrl = request.imageUrl
        updatedAt = ZonedDateTime.now()
    }

    fun delete() {
        isDeleted = true
    }

    companion object {
        fun of(
            name: String,
            price: Long,
            description: String,
            category: String,
            stock: Int,
            discountRate: Int,
            imageUrl: String?
        ): Product {
            val timestamp = ZonedDateTime.now(ZoneId.of("Asia/Seoul"))
            return Product(
                name = name,
                price = price,
                description = description,
                category = Category.fromString(category),
                status = Status.calc(stock, discountRate),
                stock = stock,
                discountRate = discountRate,
                imageUrl = imageUrl,
                createdAt = timestamp,
                updatedAt = timestamp,
                isDeleted = false
            )
        }
    }
}

fun Product.toResponse(): ProductResponseDto {
    return ProductResponseDto(
        id = id!!,
        name = name,
        price = price,
        description = description,
        category = category.name,
        stock = stock,
        status = status.name,
        discountRate = discountRate,
        imageUrl = imageUrl,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
}

fun Product.toDetailResponse(): ProductDetailResponseDto {
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

