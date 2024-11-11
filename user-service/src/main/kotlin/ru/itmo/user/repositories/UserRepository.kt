package ru.itmo.user.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.itmo.user.model.entity.UserEntity
import java.util.*

@Repository
interface UserRepository : JpaRepository<UserEntity, Long> {
    fun findByUsername(username: String): Optional<UserEntity>
    override fun deleteById(id: Long)
    fun existsByUsername(username: String): Boolean
    override fun existsById(id: Long): Boolean
    fun getUserEntityById(id: Long): UserEntity
    fun getUserEntityByUsername(username: String?): UserEntity
}