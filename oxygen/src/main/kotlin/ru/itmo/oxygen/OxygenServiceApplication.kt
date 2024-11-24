package ru.itmo.oxygen

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class OxygenServiceApplication

fun main(args: Array<String>) {
    runApplication<OxygenServiceApplication>(*args)
}
