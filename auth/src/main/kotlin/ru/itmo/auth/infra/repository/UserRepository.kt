package ru.itmo.auth.infra.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.itmo.auth.infra.model.User
import java.util.*

interface UserRepository : JpaRepository<User, UUID> {

    fun findByLogin(login: String): Optional<User>
}