package ru.itmo.auth

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.netflix.hystrix.EnableHystrix
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableFeignClients
@EnableHystrix
@EnableDiscoveryClient
class AuthApplication

fun main(args: Array<String>) {
    runApplication<AuthApplication>(*args)
}
