package ru.itmo.highload_systems.service

import org.springframework.stereotype.Service
import ru.itmo.highload_systems.api.dto.CreateOrderRequest
import ru.itmo.highload_systems.api.dto.OrderResponse
import ru.itmo.highload_systems.model.entity.Order
import ru.itmo.highload_systems.model.entity.OrderRoom
import ru.itmo.highload_systems.model.enums.OrderStatus
import ru.itmo.highload_systems.repository.OrderRepository
import ru.itmo.highload_systems.repository.OrderRoomRepository
import ru.itmo.highload_systems.repository.RoomRepository
import java.util.*

@Service
class OrderService(
    private val orderRepository: OrderRepository,
    private val roomRepository: RoomRepository,
    private val orderRoomRepository: OrderRoomRepository
) {
//
//    fun create(request: CreateOrderRequest): OrderResponse {
//
//    }

    fun findById(id: UUID): Order {
        return orderRepository.findById(id).orElseThrow();
    }

    fun findAll() {

    }

    fun cancel() {

    }
//
//    fun process(id: UUID): Order {
//        val order = findById(id)
//        if (order.status != OrderStatus.NEW) {
//            throw IllegalArgumentException();
//        }
//        val room = roomRepository.findFirstBySizeMoreOrEqualThan(order.dayCount * order.dailyNorm);
//        if (room.isEmpty) {
//            order.status = OrderStatus.OXYGEN_WAITING
//            return orderRepository.save(order)
//        }
//        val orderRoom = OrderRoom(
//            room = room.get(),
//            order = order
//        )
//        orderRoomRepository.save(orderRoom)
//        order.status = OrderStatus.DONE
//        return orderRepository.save(order)
//    }
}