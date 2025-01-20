package ru.itmo.order.domain.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.itmo.order.api.dto.OrderResponse
import ru.itmo.order.asyncapi.OrderPublisher
import ru.itmo.order.clients.DepartmentClient
import ru.itmo.order.clients.UserClient
import ru.itmo.order.clients.dto.CheckInResponse
import ru.itmo.order.domain.mapper.OrderApiMapper
import ru.itmo.order.infra.model.Order
import ru.itmo.order.infra.model.enums.OrderStatus
import ru.itmo.order.infra.repository.OrderRepository
import java.time.OffsetDateTime
import java.util.*

@Service
@Transactional(readOnly = true)
class OrderService(
    private val orderRepository: OrderRepository,
    private val orderApiMapper: OrderApiMapper,
    private val userClient: UserClient,
    private val departmentClient: DepartmentClient,
    private val orderPublisher: OrderPublisher
) {

    @Transactional(readOnly = false)
    fun create(departmentId: UUID, userId: UUID): Flux<OrderResponse> {
        userClient.getById(userId)
        if (orderRepository.existsByUserIdAndStatusNotIn(userId, listOf(OrderStatus.CANCEL))) {
            throw IllegalArgumentException("Заявка на пользователя с id $userId уже зарегистрирована")
        }
        return Flux.just(
            orderApiMapper.toResponse(
                orderRepository.save(
                    Order(
                        departmentId = departmentId,
                        userId = userId
                    )
                )
            )
        )
            .flatMap { response ->
                Mono.fromCallable {
                    orderPublisher.send(orderApiMapper.toEvent(response))
                }
                    .thenReturn(response)
            }
    }

    @Transactional(readOnly = false)
    fun process(id: UUID, token: String): Mono<CheckInResponse> {
        return findEntityById(id)
            .map { order -> order.get() }
            .flatMap { order ->
                if (order.status != OrderStatus.NEW) {
                    return@flatMap Mono.error(
                        IllegalArgumentException(
                            "Заявка в статусе ${order.status} не может быть обработана"
                        )
                    )
                }
                departmentClient.checkIn(token, order.departmentId!!, order.userId!!)
                    .let { checkInResponse -> Mono.just(checkInResponse) }
                    .flatMap { checkInResponse ->
                        order.status = OrderStatus.DONE
                        order.onSaveHook()
                        val saved = orderRepository.save(order)
                        return@flatMap Mono.fromCallable {
                            orderPublisher.send(orderApiMapper.toEvent(saved))
                        }
                            .thenReturn(checkInResponse)
                    }
            }
    }

    @Transactional(readOnly = false)
    fun cancelExpiredOrders(
        expiredAt: OffsetDateTime
    ): Flux<OrderResponse> {
        return Flux.fromIterable(orderRepository.findAllByUpdatedAtLessThan(expiredAt))
            .filter { order -> order.status == OrderStatus.NEW }
            .map { order ->
                order.status = OrderStatus.CANCEL
                order
            }
            .collectList()
            .flatMapMany { orders ->
                Flux.fromIterable(orderRepository.saveAll(orders))
            }
            .map { order ->
                orderPublisher.send(orderApiMapper.toEvent(order))
                return@map orderApiMapper.toResponse(order)
            }
    }

    @Transactional(readOnly = false)
    fun cancelById(id: UUID): Mono<OrderResponse> {
        return findEntityById(id)
            .map { order -> order.get() }
            .handle { order, sink ->
                if (order.status == OrderStatus.NEW) {
                    order.status = OrderStatus.CANCEL
                    val savedOrder = orderRepository.save(order)
                    orderPublisher.send(orderApiMapper.toEvent(savedOrder))
                    sink.next(orderApiMapper.toResponse(savedOrder))
                    return@handle
                }
                sink.error(
                    IllegalArgumentException(
                        "Заявка в статусе ${order.status} не может быть отменена"
                    )
                )
            }
    }

    fun findAll(pageable: Pageable): Flux<Page<OrderResponse>> {
        return Flux.just(orderRepository.findAll(pageable))
            .map { it -> it.map { orderApiMapper.toResponse(it) } }
    }

    fun findById(id: UUID): Mono<OrderResponse> {
        return findEntityById(id)
            .map { order -> orderApiMapper.toResponse(order.get()) }
    }

    private fun findEntityById(id: UUID): Mono<Optional<Order>> {
        val order = orderRepository.findById(id)
        if (order.isEmpty) {
            throw NoSuchElementException("Заявка c id %s не найдена".format(id))
        }
        return Mono.just(order)
    }
}