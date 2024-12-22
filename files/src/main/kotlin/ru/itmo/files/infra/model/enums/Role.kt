package ru.itmo.order.infra.model.enums

import org.springframework.security.core.GrantedAuthority

enum class Role : GrantedAuthority {
    ADMIN, USER, SUPPLIER, MANAGER;

    override fun getAuthority(): String {
        return name
    }
}