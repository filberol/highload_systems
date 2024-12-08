package ru.itmo.oxygen.clients

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam
import ru.itmo.oxygen.clients.dto.RoomNormResponse
import ru.itmo.oxygen.clients.exception.InternalServerException
import java.util.*

@FeignClient(
    name = "department",
    url = "gateway:8080",
    fallback = DepartmentClient.DepartmentClientFallback::class
)
interface DepartmentClient {

    @PostMapping("/rooms/{id}/supply-oxygen")
    fun supplyOxygen(
        @RequestHeader("Authorization") token: String,
        @PathVariable id: UUID,
        @RequestParam("size") size: Long
    ): ResponseEntity<RoomNormResponse>

    class DepartmentClientFallback : DepartmentClient {

        override fun supplyOxygen(
            token: String,
            id: UUID,
            size: Long
        ): ResponseEntity<RoomNormResponse> {
            throw InternalServerException("Department service not available")
        }
    }
}