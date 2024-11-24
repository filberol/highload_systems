package ru.itmo.order.infra.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.itmo.order.infra.model.Order
import java.time.OffsetDateTime
import java.util.*

@Repository
interface OrderRepository : JpaRepository<Order, UUID> {

    fun findAllByUpdatedAtLessThan(
        expiredAt: OffsetDateTime
    ): List<Order>

}