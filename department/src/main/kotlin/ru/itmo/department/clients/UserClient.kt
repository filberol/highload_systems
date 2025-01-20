package ru.itmo.department.clients

import feign.FeignException
import feign.RetryableException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.cloud.openfeign.FallbackFactory
import org.springframework.cloud.openfeign.FeignClient
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
    fun getById(@PathVariable id: UUID): UserResponse

    @Component
    class UserClientFallbackFactory : FallbackFactory<UserClient> {

        private val logger: Logger
            get() = LoggerFactory.getLogger(UserClientFallbackFactory::class.java)

        override fun create(cause: Throwable?): UserClient {
            logger.error(cause?.message)
            logger.error(cause?.cause?.message)
            if (cause is FeignException.FeignServerException || cause is RetryableException) {
                return UserClientServerFallback()
            }
            return UserClientFallback()
        }
    }

    class UserClientFallback : UserClient {
        @GetMapping("users/id/{id}")
        override fun getById(@PathVariable id: UUID): UserResponse {
            throw IllegalArgumentException("Пользователь с id $id не найден")
        }
    }

    class UserClientServerFallback : UserClient {
        @GetMapping("users/id/{id}")
        override fun getById(@PathVariable id: UUID): UserResponse {
            throw IllegalStateException("Auth Service не доступен")
        }
    }
}
