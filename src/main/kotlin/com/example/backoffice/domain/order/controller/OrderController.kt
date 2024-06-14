package com.example.backoffice.domain.order.controller

import com.example.backoffice.domain.order.dto.OrderRequest
import com.example.backoffice.domain.order.service.OrderService
import com.example.backoffice.infra.security.MemberPrincipal
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/order")
class OrderController(
    private val orderService: OrderService
) {

    @PostMapping("")
    fun placeOrder(
        @AuthenticationPrincipal principal: MemberPrincipal,
        @RequestBody orderRequest: List<OrderRequest>
    ) {
        orderService.placeOrder(principal.id, orderRequest)
    }
}