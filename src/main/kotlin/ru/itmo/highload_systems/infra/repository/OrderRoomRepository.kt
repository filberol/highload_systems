package ru.itmo.highload_systems.infra.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.itmo.highload_systems.infra.model.OrderRoom
import java.util.UUID

interface OrderRoomRepository : JpaRepository<OrderRoom, UUID> {
}