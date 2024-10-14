package ru.itmo.highload_systems.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.DriverManagerDataSource
import javax.sql.DataSource


@Configuration
class DatabaseConfig {

    @Bean
    fun dataSource(
        @Value("\${spring.datasource.url}") url: String?,
        @Value("\${spring.datasource.username}") username: String?,
        @Value("\${spring.datasource.password}") password: String?,
        @Value("\${spring.datasource.driverClassName}") driverClassName: String?
    ): DataSource {
        val driverManagerDataSource = DriverManagerDataSource()
        driverManagerDataSource.url = url
        driverManagerDataSource.username = username
        driverManagerDataSource.password = password
        driverManagerDataSource.setDriverClassName(driverClassName!!)
        return driverManagerDataSource
    }
}