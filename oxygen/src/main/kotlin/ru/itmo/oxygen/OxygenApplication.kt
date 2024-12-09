package ru.itmo.oxygen

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.netflix.hystrix.EnableHystrix
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableFeignClients
@EnableHystrix
@EnableDiscoveryClient
class OxygenApplication

fun main(args: Array<String>) {
    runApplication<OxygenApplication>(*args)
}
