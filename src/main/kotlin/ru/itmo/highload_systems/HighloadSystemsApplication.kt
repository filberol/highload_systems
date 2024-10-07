package ru.itmo.highload_systems

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class HighloadSystemsApplication

fun main(args: Array<String>) {
    runApplication<HighloadSystemsApplication>(*args)
}
