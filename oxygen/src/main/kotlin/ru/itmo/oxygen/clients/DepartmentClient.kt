package ru.itmo.oxygen.clients

import feign.FeignException
import feign.RetryableException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.cloud.openfeign.FallbackFactory
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam
import ru.itmo.oxygen.clients.dto.RoomNormResponse
import java.util.*

@FeignClient(
    name = "department",
    url = "gateway:8080",
    fallbackFactory = DepartmentClient.DepartmentClientFallbackFactory::class
)
interface DepartmentClient {

    @PostMapping("/rooms/{id}/supply-oxygen")
    fun supplyOxygen(
        @RequestHeader("Authorization") token: String,
        @PathVariable id: UUID,
        @RequestParam("size") size: Long
    ): RoomNormResponse

    @Component
    class DepartmentClientFallbackFactory : FallbackFactory<DepartmentClient> {
        private val logger: Logger
            get() = LoggerFactory.getLogger(DepartmentClientFallbackFactory::class.java)

        override fun create(cause: Throwable?): DepartmentClient {
            logger.error(cause?.message)
            logger.error(cause?.cause?.message)
            if (cause is FeignException.FeignServerException || cause is RetryableException) {
                return DepartmentClientServerFallback()
            }
            return DepartmentClientFallback()
        }
    }

    class DepartmentClientServerFallback : DepartmentClient {
        @PostMapping("/rooms/{id}/supply-oxygen")
        override fun supplyOxygen(
            @RequestHeader("Authorization") token: String,
            @PathVariable id: UUID,
            @RequestParam("size") size: Long
        ): RoomNormResponse {
            throw IllegalStateException("Department Service не доступен")
        }
    }

    class DepartmentClientFallback : DepartmentClient {
        @PostMapping("/rooms/{id}/supply-oxygen")
        override fun supplyOxygen(
            @RequestHeader("Authorization") token: String,
            @PathVariable id: UUID,
            @RequestParam("size") size: Long
        ): RoomNormResponse {
            throw IllegalArgumentException("Перевозка в департамент id $id не доступна")
        }
    }
}