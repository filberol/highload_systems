package ru.itmo.user.services

import org.springframework.stereotype.Service
import ru.itmo.user.model.entity.RoleEntity
import ru.itmo.user.model.enums.Role
import ru.itmo.user.repositories.RoleRepository

@Service
class RoleService(
    private val repository: RoleRepository
) {


    fun findByRole(role: Role): RoleEntity {
        return repository.findByRole(role).orElseThrow()
    }

    val standardUserRole: RoleEntity
        get() = findByRole(Role.STANDARD_USER)

    val adminRole: RoleEntity
        get() = findByRole(Role.ADMIN)
    val premiumUserRole: RoleEntity
        get() = findByRole(Role.PREMIUM_USER)
    val blockedUserRole: RoleEntity
        get() = findByRole(Role.BLOCKED_USER)
}