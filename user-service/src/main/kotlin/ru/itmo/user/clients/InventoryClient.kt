package ru.itmo.user.clients

import jakarta.validation.constraints.Positive
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import ru.itmo.user.exceptions.ServiceUnavailableException


@FeignClient(
    name = "inventory-client",
    url = "gateway-server:8081",
    fallback = InventoryClient.InventoryClientFallback::class
)
interface InventoryClient {
    @DeleteMapping("inventory/delete/{userId}")
    fun deleteAll(@PathVariable @Positive userId: Long?): ResponseEntity<String>

    @Component
    class InventoryClientFallback : InventoryClient {
        override fun deleteAll(@PathVariable @Positive userId: Long?): ResponseEntity<String> {
            throw ServiceUnavailableException("Items service not available")
        }
    }
}