package com.example.backoffice.domain.order.controller

import com.example.backoffice.domain.order.dto.OrderDetailsResponse
import com.example.backoffice.domain.order.dto.OrderRequest
import com.example.backoffice.domain.order.dto.OrdersResponse
import com.example.backoffice.domain.order.service.OrderService
import com.example.backoffice.infra.security.CustomPreAuthorize
import com.example.backoffice.infra.security.MemberPrincipal
import com.example.backoffice.infra.security.MemberRole

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/orders")
class OrderController(
    private val orderService: OrderService,
    private val preAuthorize: CustomPreAuthorize
) {

    @PostMapping("")
    fun placeOrder(
        @AuthenticationPrincipal principal: MemberPrincipal,
        @RequestBody orderRequest: List<OrderRequest>
    ): ResponseEntity<Long?> = preAuthorize.hasAnyRole(principal, setOf(MemberRole.USER)) {
        ResponseEntity.status(HttpStatus.OK).body(
            orderService.placeOrder(principal.id, orderRequest)
        )
    }

    @GetMapping("/{orderId}")
    fun getOrder(
        @AuthenticationPrincipal principal: MemberPrincipal,
        @PathVariable orderId: Long
    ): ResponseEntity<OrderDetailsResponse> = preAuthorize.hasAnyRole(principal, setOf(MemberRole.USER)) {
        ResponseEntity.status(HttpStatus.OK).body(
            orderService.getOrderDetails(principal.id, orderId)
        )
    }

    @GetMapping("")
    fun getOrders(
        @AuthenticationPrincipal principal: MemberPrincipal
    ): ResponseEntity<OrdersResponse> = preAuthorize.hasAnyRole(principal, setOf(MemberRole.USER)) {
        ResponseEntity.status(HttpStatus.OK).body(
            orderService.getOrders(principal.id)
        )
    }

    @PatchMapping("/{orderId}")
    fun cancelOrder(
        @AuthenticationPrincipal principal: MemberPrincipal,
        @PathVariable orderId: Long
    ): ResponseEntity<Unit> = preAuthorize.hasAnyRole(principal, setOf(MemberRole.USER)) {
        ResponseEntity.status(HttpStatus.OK).body(
            orderService.cancelOrder(principal.id, orderId)
        )
    }

}