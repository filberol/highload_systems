package ru.itmo.auth.infra.model.enums

import org.springframework.security.core.GrantedAuthority

enum class UserRole : GrantedAuthority {
    ADMIN, USER, SUPPLIER, MANAGER;

    override fun getAuthority(): String {
        return name
    }
}