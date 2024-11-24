package ru.itmo.auth.config.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.web.filter.OncePerRequestFilter
import ru.itmo.auth.domain.mapper.UserMapper
import ru.itmo.auth.domain.service.JwtService
import ru.itmo.auth.domain.service.UserService
import java.io.IOException


class JwtFilter(
    private val jwtService: JwtService,
    private val userService: UserService,
    private val userMapper: UserMapper
) : OncePerRequestFilter() {
    private val BEARER = "Bearer"

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader(HttpHeaders.AUTHORIZATION)
        if (authHeader == null || !authHeader.startsWith(BEARER)) {
            filterChain.doFilter(request, response)
            return
        }
        val jwt = authHeader.substring(BEARER.length + 1)
        val login: String = jwtService.extractLogin(jwt)
        val user = userService.findByLogin(login)
        val userDetails = userMapper.toDetails(user)
        if (jwtService.isTokenValid(jwt, userDetails)) {
            val authToken = UsernamePasswordAuthenticationToken(
                userDetails,
                userDetails.getPassword(),
                userDetails.authorities
            )
            authToken.details =
                WebAuthenticationDetailsSource().buildDetails(request) // session and ip
            SecurityContextHolder.getContext().authentication = authToken
            request.setAttribute("userId", user.id)
            request.setAttribute("username", userDetails.username)
            request.setAttribute("authorities", userDetails.authorities)
        }
        filterChain.doFilter(request, response)
    }
}
