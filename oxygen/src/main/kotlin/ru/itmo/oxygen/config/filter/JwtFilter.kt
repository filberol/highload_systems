package ru.itmo.oxygen.config.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import ru.itmo.oxygen.infra.model.enums.Role
import java.io.IOException


class JWTVerifierFilter : OncePerRequestFilter() {
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
        val username = request.getHeader("username")
        val userRole = Role.valueOf(request.getHeader("authorities"))
        val authentication: Authentication =
            UsernamePasswordAuthenticationToken(username, null, listOf(userRole))
        SecurityContextHolder.getContext().authentication = authentication
        filterChain.doFilter(request, response)
    }
}