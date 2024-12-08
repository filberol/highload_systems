package ru.itmo.oxygen.common.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Import

@TestConfiguration
@ComponentScan(basePackages = ["ru.itmo.oxygen.domain.mapper"])
@Import(ObjectMapper::class)
class DomainMapperTestConfiguration {

    @Autowired
    protected var objectMapper: ObjectMapper? = null
}