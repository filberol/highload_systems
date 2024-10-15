package ru.itmo.highload_systems.common.config

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Bean
import org.springframework.test.context.DynamicPropertyRegistry
import org.testcontainers.containers.PostgreSQLContainer


@TestConfiguration
class TestContainersConfiguration {

    @Bean
    @ServiceConnection
    fun container(registry: DynamicPropertyRegistry): PostgreSQLContainer<*> {
        val container: PostgreSQLContainer<*> = PostgreSQLContainer("postgres:14")
            .withDatabaseName("")
            .withReuse(true)
        with(registry) {
            add("spring.datasource.url") { container.jdbcUrl }
            add("spring.datasource.username") { container.username }
            add("spring.datasource.password") { container.password }
            add("spring.liquibase.url") { container.jdbcUrl }
            add("spring.liquibase.user") { container.username }
            add("spring.liquibase.password") { container.password }
        }
        return container
    }
}

@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfiguration