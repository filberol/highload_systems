package ru.itmo.highload_systems

import org.springframework.boot.fromApplication
import org.springframework.boot.with
import ru.itmo.highload_systems.common.config.TestContainersConfiguration


fun main(args: Array<String>) {
    fromApplication<HighloadSystemsApplication>().with(TestContainersConfiguration::class).run(*args)
}
