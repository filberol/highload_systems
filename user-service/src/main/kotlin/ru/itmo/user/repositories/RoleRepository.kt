package ru.itmo.user.repositories

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.itmo.user.model.entity.RoleEntity
import ru.itmo.user.model.enums.Role
import java.util.*

@Repository
interface RoleRepository : CrudRepository<RoleEntity, Int> {
    fun findByRole(role: Role): Optional<RoleEntity>
}