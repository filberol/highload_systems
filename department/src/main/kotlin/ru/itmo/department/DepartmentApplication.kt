package ru.itmo.department

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.hystrix.EnableHystrix
import org.springframework.cloud.openfeign.EnableFeignClients


@SpringBootApplication
@EnableFeignClients
@EnableHystrix
class DepartmentApplication

fun main(args: Array<String>) {
    runApplication<DepartmentApplication>(*args)
}