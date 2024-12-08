package ru.itmo.department.clients

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import ru.itmo.department.clients.dto.UserResponse
import java.util.*

@FeignClient(name = "auth", url = "localhost:8080", fallback = UserClientFallback::class)
interface UserClient {

    @GetMapping("users/id/{id}")
    fun getById(@PathVariable id: UUID): ResponseEntity<UserResponse>
}

@Component
class UserClientFallback : UserClient {
    override fun getById(@PathVariable id: UUID): ResponseEntity<UserResponse> {
        throw IllegalArgumentException("Auth service not available")
    }
}