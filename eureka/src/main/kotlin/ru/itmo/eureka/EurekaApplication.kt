package ru.itmo.eureka

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer
import org.springframework.cloud.netflix.hystrix.EnableHystrix

@SpringBootApplication
@EnableEurekaServer
@EnableHystrix
class EurekaApplication

fun main(args: Array<String>) {
    runApplication<EurekaApplication>(*args)
}
