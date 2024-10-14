package ru.itmo.highload_systems

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class HighloadSystemsApplication{

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(HighloadSystemsApplication::class.java, *args)
        }
    }
}
