package ru.itmo.highload_systems

import org.springframework.boot.fromApplication
import org.springframework.boot.with


fun main(args: Array<String>) {
    fromApplication<HighloadSystemsApplication>().with(TestcontainersConfiguration::class).run(*args)
}
