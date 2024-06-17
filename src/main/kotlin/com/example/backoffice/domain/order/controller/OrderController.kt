package com.example.backoffice.domain.order.controller

import com.example.backoffice.domain.order.dto.OrderDetailsResponse
import com.example.backoffice.domain.order.dto.OrderRequest
import com.example.backoffice.domain.order.service.OrderService
import com.example.backoffice.infra.security.MemberPrincipal
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/orders")
class OrderController(
    private val orderService: OrderService
) {

    @PostMapping("")
    fun placeOrder(
        @AuthenticationPrincipal principal: MemberPrincipal,
        @RequestBody orderRequest: List<OrderRequest>
    ): ResponseEntity<Long?> {
        return ResponseEntity.status(HttpStatus.OK).body(
            orderService.placeOrder(principal.id, orderRequest)
        )
    }

    @GetMapping("/{orderId}")
    fun getOrder(
        @AuthenticationPrincipal principal: MemberPrincipal,
        @PathVariable orderId: Long
    ): ResponseEntity<OrderDetailsResponse> {
        return ResponseEntity.status(HttpStatus.OK).body(
            orderService.getOrderDetails(principal.id, orderId)
        )
    }
}