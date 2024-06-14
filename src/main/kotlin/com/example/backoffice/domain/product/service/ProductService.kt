package com.example.backoffice.domain.product.service

import com.example.backoffice.common.exception.ModelNotFoundException
import com.example.backoffice.domain.product.dto.*
import com.example.backoffice.domain.product.model.Category
import com.example.backoffice.domain.product.model.Product
import com.example.backoffice.domain.product.repository.ProductRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductService(
    private val productRepository: ProductRepository,
) {
    fun getProducts(
        pageSize: Long,
        sorted: String?,
        cursor: Long,
        category: Category?,
        name: String?,
        onDiscount: Boolean?
    ): List<ProductResponseDto> {
        return productRepository.findByPageableAndDeleted(pageSize, sorted, cursor, category, name, onDiscount)
            .map { it.toResponse() }
    }

    fun getProductById(productId: Long): ProductDetailResponseDto {
        val product = productRepository.findByIdAndDeletedAtIsNull(productId) ?: throw ModelNotFoundException(
            "Product",
            productId
        )
        return product.toDetailResponse()
    }

    @Transactional
    fun createProduct(request: CreateProductRequestDto): IdResponseDto {
        val product = Product.of(
            name = request.name,
            price = request.price,
            description = request.description,
            category = request.category,
            stock = request.stock,
            discountRate = request.discountRate,
            imageUrl = request.imageUrl
        )
        return productRepository.save(product).toIdResponse()
    }

    @Transactional
    fun updateProduct(productId: Long, request: UpdateProductRequestDto): IdResponseDto {
        val product = productRepository.findByIdAndDeletedAtIsNull(productId) ?: throw ModelNotFoundException(
            "Product",
            productId
        )
        product.update(
            name = request.name,
            price = request.price,
            description = request.description,
            category = request.category,
            stock = request.stock,
            discountRate = request.discountRate,
            imageUrl = request.imageUrl
        )
        return product.toIdResponse()
    }

    @Transactional
    fun deleteProduct(productId: Long) {
        val product = productRepository.findByIdAndDeletedAtIsNull(productId) ?: throw ModelNotFoundException(
            "Product",
            productId
        )
        product.delete()
    }

}
