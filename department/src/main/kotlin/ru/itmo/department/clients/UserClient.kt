package ru.itmo.department.clients

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import ru.itmo.department.clients.dto.UserResponse
import ru.itmo.department.clients.exception.InternalServerException
import java.util.*

@FeignClient(name = "auth", url = "gateway:8080", fallback = UserClient.UserClientFallback::class)
interface UserClient {

    @GetMapping("users/id/{id}")
    fun getById(@PathVariable id: UUID): ResponseEntity<UserResponse>

    class UserClientFallback : UserClient {
        override fun getById(id: UUID): ResponseEntity<UserResponse> {
            throw InternalServerException("Auth service not available")
        }
    }
}