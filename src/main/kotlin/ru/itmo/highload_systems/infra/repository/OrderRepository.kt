package ru.itmo.highload_systems.infra.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.itmo.highload_systems.infra.model.Order
import ru.itmo.highload_systems.infra.model.enums.OrderStatus
import java.time.OffsetDateTime
import java.util.*

@Repository
interface OrderRepository : JpaRepository<Order, UUID> {

    fun findAllByUpdatedAtLessThanAndStatusEquals(
        expiredAt: OffsetDateTime,
        status: OrderStatus
    ): List<Order>

}