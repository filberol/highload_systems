package ru.itmo.order.clients

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import ru.itmo.order.clients.dto.UserResponse
import ru.itmo.order.clients.exception.InternalServerException
import java.util.*

@FeignClient(name = "auth", url = "localhost:8080", fallback = UserClient.UserClientFallback::class)
interface UserClient {

    @GetMapping("users/id/{id}")
    fun getById(@PathVariable id: UUID): ResponseEntity<UserResponse>

    class UserClientFallback : UserClient {
        override fun getById(id: UUID): ResponseEntity<UserResponse> {
            throw InternalServerException("Auth service not available")
        }
    }
}