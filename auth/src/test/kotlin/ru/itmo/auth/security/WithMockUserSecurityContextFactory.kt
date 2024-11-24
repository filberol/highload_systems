package ru.itmo.auth.security

import jakarta.persistence.EntityNotFoundException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.test.context.support.WithSecurityContextFactory
import ru.itmo.auth.infra.model.User
import ru.itmo.auth.infra.repository.UserRepository
import java.util.*

class WithMockUserSecurityContextFactory(private val userService: UserRepository) :
    WithSecurityContextFactory<WithMockUser> {

    override fun createSecurityContext(withMockUser: WithMockUser): SecurityContext {
        val userOptional: Optional<User> = userService.findByLogin(withMockUser.login)
        return userOptional.map { user ->
            val authentication: Authentication = UsernamePasswordAuthenticationToken.authenticated(
                user.login,
                user.password,
                listOf(user.role)
            )
            val securityContext = SecurityContextHolder.createEmptyContext()
            securityContext.authentication = authentication
            securityContext
        }.orElseThrow { EntityNotFoundException() }
    }
}