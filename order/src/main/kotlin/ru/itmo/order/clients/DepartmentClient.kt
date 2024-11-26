package ru.itmo.order.clients

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import reactor.core.publisher.Mono
import ru.itmo.order.clients.dto.CheckInResponse
import ru.itmo.order.clients.exception.InternalServerException
import java.util.*

@FeignClient(
    name = "department",
    url = "gateway:8080",
    fallback = DepartmentClient.DepartmentClientFallback::class
)
interface DepartmentClient {

    @PostMapping("/departments/{id}/check-in")
    fun checkIn(@PathVariable id: UUID, @RequestParam userId: UUID): Mono<CheckInResponse>

    class DepartmentClientFallback : DepartmentClient {
        override fun checkIn(id: UUID, userId: UUID): Mono<CheckInResponse> {
            throw InternalServerException("Department service not available")
        }

    }

}