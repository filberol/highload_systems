package ru.itmo.order.config.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException

@Component
class JwtFilter : OncePerRequestFilter() {
    private val BEARER = "Bearer"

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader(org.springframework.http.HttpHeaders.AUTHORIZATION)
        if (authHeader == null || !authHeader.startsWith(BEARER)) {
            filterChain.doFilter(request, response)
            return
        }
        val jwt = authHeader.substring(7).trim()
        val username = JwtTokenUtils().getUsername(jwt)
        val userRole = JwtTokenUtils().getRoles(jwt)!!
        val authentication: Authentication =
            UsernamePasswordAuthenticationToken(
                username,
                null,
                userRole.stream().map { role -> SimpleGrantedAuthority(role) }.toList()
            )
        SecurityContextHolder.getContext().authentication = authentication
        filterChain.doFilter(request, response)
    }
}