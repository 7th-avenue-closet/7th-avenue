package com.example.backoffice.domain.product.service

import com.example.backoffice.domain.product.dto.*
import com.example.backoffice.domain.product.model.*
import com.example.backoffice.domain.product.repository.ProductRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.ZonedDateTime


@Service
class ProductService (
    private val productRepository: ProductRepository,
) {
    fun getProducts(): List<ProductResponseDto> {
        return productRepository.findAll().map { it.toResponse()}
    }

    fun getProductById(productId: Long): ProductDetailResponseDto {
        val product = productRepository.findByIdOrNull(productId) ?: throw RuntimeException("Product with ID $productId not found")
        return product.toDetailResponse()
    }

    @Transactional
    fun createProduct(request: CreateProductRequestDto): ProductResponseDto {

        return productRepository.save(
            Product(
                name = request.name,
                price = request.price,
                description = request.description,
                category = Category.fromString(request.category),
                status = Status.calc(request.stock, request.discountRate),
                stock = request.stock,
                discountRate = request.discountRate,
                imageUrl = request.imageUrl,
                createdAt = ZonedDateTime.now(),
                updatedAt = ZonedDateTime.now()
            )
        ).toResponse()
    }

    fun updateProduct(productId: Long, request: UpdateProductRequestDto) : IdResponseDto {
        val product = productRepository.findByIdOrNull(productId) ?: throw RuntimeException("Product with ID $productId not found")
        product.update(request)
        return product.toIdResponse()
    }

    fun deleteProduct(productId: Long) {
        val product = productRepository.findByIdOrNull(productId) ?: throw RuntimeException("Product with ID $productId not found")
        product.delete()
        productRepository.save(product)
    }

}
