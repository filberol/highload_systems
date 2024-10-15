package ru.itmo.highload_systems.common.config

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Import
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper

@TestConfiguration
@ComponentScan(basePackages = ["ru.itmo.highload_systems.domain.mapper"])
@Import(ObjectMapper::class)
class ApiMappersTestConfiguration