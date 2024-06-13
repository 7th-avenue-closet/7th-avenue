package com.example.backoffice.domain.product.service

import com.example.backoffice.common.exception.ModelNotFoundException
import com.example.backoffice.domain.product.dto.*
import com.example.backoffice.domain.product.model.*
import com.example.backoffice.domain.product.repository.ProductRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.ZonedDateTime


@Service
class ProductService(
    private val productRepository: ProductRepository,
) {
    fun getProducts(): List<ProductResponseDto> {
        return productRepository.findAll().map { it.toResponse() }
    }

    fun getProductById(productId: Long): ProductDetailResponseDto {
        val product = productRepository.findByIdOrNull(productId) ?: throw ModelNotFoundException("Product", productId)
        return product.toDetailResponse()
    }

    @Transactional
    fun createProduct(request: CreateProductRequestDto) {
        val product = Product.of(
            name = request.name,
            price = request.price,
            description = request.description,
            category = request.category,
            stock = request.stock,
            discountRate = request.discountRate,
            imageUrl = request.imageUrl
        )
        productRepository.save(product)
    }

    @Transactional
    fun updateProduct(productId: Long, request: UpdateProductRequestDto): Long {
        val product = productRepository.findByIdOrNull(productId) ?: throw ModelNotFoundException("Product", productId)
        product.update(request)
        return product.id!!
    }

    @Transactional
    fun deleteProduct(productId: Long) {
        val product = productRepository.findByIdOrNull(productId) ?: throw ModelNotFoundException("Product", productId)
        product.delete()
    }

}
