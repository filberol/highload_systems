package ru.itmo.file

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.netflix.hystrix.EnableHystrix
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication(exclude = [SecurityAutoConfiguration::class, SecurityFilterAutoConfiguration::class, DataSourceAutoConfiguration::class])
@EnableFeignClients
@EnableHystrix
@EnableDiscoveryClient
@ConfigurationPropertiesScan
class FileApplication

fun main(args: Array<String>) {
    runApplication<FileApplication>(*args)
}

