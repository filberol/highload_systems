package ru.itmo.order.clients

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam
import ru.itmo.order.clients.dto.CheckInResponse
import java.util.*

@FeignClient(
    name = "department",
    url = "gateway:8080",
    fallback = DepartmentClientFallback::class
)
interface DepartmentClient {

    @PostMapping("/departments/{id}/check-in")
    fun checkIn(
        @RequestHeader("Authorization") token: String,
        @PathVariable id: UUID,
        @RequestParam userId: UUID
    ): CheckInResponse
}

@Component
class DepartmentClientFallback : DepartmentClient {
    override fun checkIn(
        @RequestHeader("Authorization") token: String,
        @PathVariable id: UUID,
        @RequestParam userId: UUID
    ): CheckInResponse {
        throw IllegalArgumentException("Заселение в департамент id $id недоступно")
    }
}