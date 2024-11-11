package ru.itmo.user.security

import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.SignatureException
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import java.util.stream.Collectors


@Component
class JwtRequestFilter(
    private val jwtTokenUtils: JwtTokenUtils
) : OncePerRequestFilter() {
    val log: Logger = LogManager.getLogger(JwtRequestFilter::class.java)


    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val header = request.getHeader(HttpHeaders.AUTHORIZATION)
        var username: String? = null
        var jwt: String? = null

        if (header != null && header.startsWith("Bearer ")) {
            jwt = header.substring(7).trim()
            try {
                username = jwtTokenUtils.getUsername(jwt)
            } catch (e: ExpiredJwtException) {
                log.debug("Token lifetime expired")
            } catch (e: SignatureException) {
                log.debug("Signature incorrect")
            }
        }
        if (username != null && SecurityContextHolder.getContext().authentication == null) {
            val token = UsernamePasswordAuthenticationToken(
                username,
                null,
                jwtTokenUtils.getRoles(jwt!!).stream().map { role: String? -> SimpleGrantedAuthority(role) }.collect(
                    Collectors.toList()
                )
            )
            SecurityContextHolder.getContext().authentication = token
        }
        filterChain.doFilter(request, response)
    }
}