package ru.itmo.department

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DepartmentServiceApplication

fun main(args: Array<String>) {
    runApplication<DepartmentServiceApplication>(*args)
}
