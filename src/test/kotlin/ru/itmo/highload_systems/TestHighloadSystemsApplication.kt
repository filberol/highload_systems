package ru.itmo.highload_systems

import org.springframework.boot.fromApplication
import org.springframework.boot.with
import ru.itmo.highload_systems.common.config.TestcontainersConfiguration


fun main(args: Array<String>) {
    fromApplication<HighloadSystemsApplication>().with(TestcontainersConfiguration::class).run(*args)
}
