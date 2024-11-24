package ru.itmo.department.security

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.test.context.support.WithSecurityContextFactory

class WithMockUserSecurityContextFactory : WithSecurityContextFactory<WithMockUser> {
    override fun createSecurityContext(withMockCustomUser: WithMockUser): SecurityContext {
        val authentication: Authentication = UsernamePasswordAuthenticationToken.authenticated(
            withMockCustomUser.username,
            withMockCustomUser.password,
            listOf(withMockCustomUser.role)
        )
        val securityContext = SecurityContextHolder.createEmptyContext()
        securityContext.authentication = authentication

        return securityContext
    }
}