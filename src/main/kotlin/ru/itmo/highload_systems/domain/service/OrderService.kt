package ru.itmo.highload_systems.domain.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.itmo.highload_systems.api.dto.CreateOrderRequest
import ru.itmo.highload_systems.api.dto.OrderResponse
import ru.itmo.highload_systems.api.dto.OrderStatusRequestResponse
import ru.itmo.highload_systems.domain.mapper.OrderApiMapper
import ru.itmo.highload_systems.domain.mapper.OrderStatusApiMapper
import ru.itmo.highload_systems.infra.model.Order
import ru.itmo.highload_systems.infra.model.enums.OrderStatus
import ru.itmo.highload_systems.infra.repository.OrderRepository
import java.security.InvalidParameterException
import java.time.OffsetDateTime
import java.util.*

@Service
@Transactional(readOnly = true)
class OrderService(
    private val departmentService: DepartmentService,
    private val orderRepository: OrderRepository,
    private val orderApiMapper: OrderApiMapper,
    private val roomNormService: RoomNormService,
    private val orderStatusApiMapper: OrderStatusApiMapper
) {

    fun create(request: CreateOrderRequest): OrderResponse {
        val department = departmentService.findById(request.departmentId)
        val order = orderRepository.save(
            Order(
                status = OrderStatus.NEW,
                size = request.size,
                department = department
            )
        )
        return orderApiMapper.toDto(order)
    }

    fun process(id: UUID): OrderResponse {
        val order = orderRepository.findById(id).orElseThrow()
        if (order.status != OrderStatus.NEW) {
            throw InvalidParameterException()
        }
        val optionalRoom =
            roomNormService.fillIfExistAndReturnRoom(order.department.id!!, order.size)
        if (optionalRoom.isEmpty) {
            order.status = OrderStatus.OXYGEN_WAITING
            return orderApiMapper.toDto(orderRepository.save(order))
        }
        order.status = OrderStatus.DONE
        order.room = optionalRoom.get()
        return orderApiMapper.toDto(orderRepository.save(order))
    }

    fun cancelExpiredOrders(
        expiredAt: OffsetDateTime,
        status: OrderStatusRequestResponse
    ): List<OrderResponse> {
        val orders = orderRepository.findAllByUpdatedAtLessThanAndStatusEquals(
            expiredAt,
            orderStatusApiMapper.toEntity(status)
        )
            .stream().map { order ->
                order.status = OrderStatus.CANCEL
                order
            }.toList()
        return orderRepository.saveAll(orders).map(orderApiMapper::toDto)
    }

    fun cancelById(id: UUID): OrderResponse {
        val order = orderRepository.findById(id).orElseThrow()
        order.status = OrderStatus.CANCEL
        return orderApiMapper.toDto(orderRepository.save(order))
    }

    fun findAll(pageable: Pageable): Page<OrderResponse> {
        return orderRepository.findAll(pageable).map(orderApiMapper::toDto)
    }

    fun findById(id: UUID): OrderResponse {
        val order = orderRepository.findById(id).orElseThrow()
        return orderApiMapper.toDto(order)
    }
}