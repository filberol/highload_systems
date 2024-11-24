package ru.itmo.gateway.api

import io.swagger.v3.oas.annotations.Hidden
import org.springdoc.core.AbstractSwaggerUiConfigProperties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.client.discovery.DiscoveryClient
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI
import java.net.URISyntaxException
import java.util.*
import java.util.function.Consumer

@Hidden
@RestController
class GatewayController() {

    @Autowired
    private lateinit var discoveryClient: DiscoveryClient

    @GetMapping("/v3/api-docs/swagger-config")
    @Throws(
        URISyntaxException::class
    )
    fun v3swaggerConfigurations(request: ServerHttpRequest): Map<String, Any> {
        val uri: URI = request.uri
        val url: URI = URI(uri.scheme, uri.getAuthority(), null, null, null)
        val swaggerConfigMap = LinkedHashMap<String, Any>()
        val swaggerUrls = LinkedList<AbstractSwaggerUiConfigProperties.SwaggerUrl>()
        discoveryClient.services.forEach(Consumer { x: String? -> println(x) })
        discoveryClient.services.stream()
            .filter { serviceName: String -> serviceName.endsWith("-service") }
            .forEach { serviceName: String ->
                swaggerUrls.add(
                    AbstractSwaggerUiConfigProperties.SwaggerUrl(
                        serviceName,
                        "$url/$serviceName/v3/api-docs",
                        serviceName
                    )
                )
            }
        swaggerConfigMap["urls"] = swaggerUrls
        return swaggerConfigMap
    }
}