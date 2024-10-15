package ru.itmo.highload_systems.infra.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.itmo.highload_systems.infra.model.OrderRoom
import java.util.UUID

@Repository
interface OrderRoomRepository : JpaRepository<OrderRoom, UUID> {
}