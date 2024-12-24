package ru.itmo.files.config.filter

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono


@Component
class JwtFilter() : WebFilter {

    private val BEARER = "Bearer"

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val authHeader = exchange.request.headers.getFirst(HttpHeaders.AUTHORIZATION)
        if (authHeader == null || !authHeader.startsWith(BEARER)) {
            return chain.filter(exchange)
        }
        val jwt = authHeader.substring(7).trim()
        return Mono.just(jwt)
            .flatMap { token ->
                val username = JwtTokenUtils().getUsername(token)
                val userRoles = JwtTokenUtils().getRoles(token)
                if (userRoles != null) {
                    val authentication = UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        userRoles.stream().map { role -> SimpleGrantedAuthority(role) }.toList()
                    )
                    ReactiveSecurityContextHolder.withAuthentication(authentication)
                    Mono.just(authentication)
                } else Mono.empty()
            }
            .switchIfEmpty(Mono.defer {
                exchange.response.statusCode = HttpStatus.UNAUTHORIZED
                Mono.empty()
            })
            .then(chain.filter(exchange))
    }
}