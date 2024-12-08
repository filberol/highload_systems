package ru.itmo.auth.config.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import ru.itmo.auth.domain.mapper.UserMapper
import ru.itmo.auth.domain.service.JwtService
import ru.itmo.auth.domain.service.UserService

@Component
class JwtFilter(
    private val jwtService: JwtService,
    private val userService: UserService,
    private val userMapper: UserMapper
) : OncePerRequestFilter() {
    private val bearer = "Bearer"

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader(HttpHeaders.AUTHORIZATION)
        if (authHeader == null || !authHeader.startsWith(bearer)) {
            filterChain.doFilter(request, response)
            return
        }
        val jwt = authHeader.substring(bearer.length + 1)
        val login: String = jwtService.extractLogin(jwt)
        val userDetails = userMapper.toEntity(userService.findByLogin(login))
        if (jwtService.isTokenValid(jwt, userDetails)) {
            val authToken = UsernamePasswordAuthenticationToken(
                userDetails,
                userDetails.getPassword(),
                userDetails.authorities
            )
            authToken.details =
                WebAuthenticationDetailsSource().buildDetails(request) // session and ip
            SecurityContextHolder.getContext().authentication = authToken
            request.setAttribute("userId", userDetails.id)
            request.setAttribute("username", userDetails.username)
            request.setAttribute("authorities", userDetails.authorities)
        }
        filterChain.doFilter(request, response)
    }
}
