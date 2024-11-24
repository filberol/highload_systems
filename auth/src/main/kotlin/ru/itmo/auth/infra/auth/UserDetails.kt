package ru.itmo.auth.infra.auth

import org.springframework.security.core.userdetails.UserDetails
import ru.itmo.auth.infra.model.enums.UserRole
import java.util.*

class UserDetails(
    val id: UUID,
    @JvmField val login: String,
    @JvmField val password: String,
    val role: UserRole
) : UserDetails {
    override fun getAuthorities(): List<UserRole> {
        return listOf(role)
    }

    override fun getPassword(): String {
        return password
    }

    override fun getUsername(): String {
        return login
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }
}