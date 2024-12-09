package ru.itmo.department.clients

import feign.FeignException.InternalServerError
import feign.FeignException.NotFound
import org.springframework.cloud.openfeign.FallbackFactory
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import ru.itmo.department.clients.dto.UserResponse
import java.util.*

@FeignClient(
    name = "auth",
    url = "gateway:8080",
    fallbackFactory = UserClient.UserClientFallbackFactory::class
)
interface UserClient {

    @GetMapping("users/id/{id}")
    fun getById(@PathVariable id: UUID): ResponseEntity<UserResponse>

    @Component
    class UserClientFallbackFactory : FallbackFactory<UserClient> {

        override fun create(cause: Throwable?): UserClient {
            if (cause is InternalServerError) {
                return UserClientServerFallback()
            }
            return UserClientFallback()
        }
    }

    class UserClientFallback : UserClient {
        @GetMapping("users/id/{id}")
        override fun getById(@PathVariable id: UUID): ResponseEntity<UserResponse> {
            throw IllegalArgumentException("Пользователь с id $id не найден")
        }
    }

    class UserClientServerFallback : UserClient {
        @GetMapping("users/id/{id}")
        override fun getById(@PathVariable id: UUID): ResponseEntity<UserResponse> {
            throw IllegalArgumentException("Auth не доступен")
        }
    }
}
