package ru.itmo.department.security

import org.springframework.security.test.context.support.WithSecurityContext
import ru.itmo.department.infra.model.enums.Role

@Retention(AnnotationRetention.RUNTIME)
@WithSecurityContext(factory = WithMockUserSecurityContextFactory::class)
annotation class WithMockUser(
    val username: String = "user",
    val password: String = "pass",
    val role: Role = Role.USER
)

