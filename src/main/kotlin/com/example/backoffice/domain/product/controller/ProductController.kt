package com.example.backoffice.domain.product.controller

import com.example.backoffice.domain.product.dto.*
import com.example.backoffice.domain.product.service.ProductService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/products")
class ProductController(
    private val productService: ProductService
) {

    @GetMapping
    fun getProducts(): ResponseEntity<List<ProductResponseDto>> {
        return ResponseEntity.status(HttpStatus.OK).body(productService.getProducts())
    }

    @GetMapping("/{productId}")
    fun getProduct(@PathVariable("productId") productId: Long): ResponseEntity<ProductDetailResponseDto> {
        return ResponseEntity.status(HttpStatus.OK).body(productService.getProductById(productId))
    }

    @PostMapping
    fun createProduct(@RequestBody request: CreateProductRequestDto): ResponseEntity<Unit> {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(request))
    }

    @PutMapping("/{productId}")
    fun updateProduct(
        @PathVariable("productId") productId: Long,
        @RequestBody request: UpdateProductRequestDto
    ): ResponseEntity<Long> {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.updateProduct(productId, request))
    }

    @DeleteMapping("/{productId}")
    fun deletePost(@PathVariable("productId") productId: Long): ResponseEntity<Unit> {
        return ResponseEntity.status(HttpStatus.OK).body(productService.deleteProduct(productId))
    }
}