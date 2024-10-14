package ru.itmo.highload_systems.api.controller

import jakarta.validation.Valid
import org.springframework.web.bind.annotation.RequestBody
import ru.itmo.highload_systems.api.dto.CreateOrderRequest
import ru.itmo.highload_systems.api.dto.OrderResponse
import ru.itmo.highload_systems.domain.service.OrderService
import java.util.UUID

class OrderController(
    private val orderService: OrderService
) {

//    fun create(@RequestBody @Valid request: CreateOrderRequest): OrderResponse {
//
//    }
//
//    fun process(id: UUID): OrderResponse {
//
//    }

}