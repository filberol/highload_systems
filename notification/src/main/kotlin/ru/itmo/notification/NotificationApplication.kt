package ru.itmo.notification

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.netflix.hystrix.EnableHystrix
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.web.socket.config.annotation.EnableWebSocket

@SpringBootApplication
@EnableFeignClients
@EnableHystrix
@EnableDiscoveryClient
@EnableWebSocket
class NotificationApplication

fun main(args: Array<String>) {
    runApplication<NotificationApplication>(*args)
}
