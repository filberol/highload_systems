package ru.itmo.auth.infra.model;

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import org.hibernate.annotations.UuidGenerator
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import ru.itmo.auth.infra.model.enums.UserRole
import java.time.OffsetDateTime
import java.util.*

@Entity
@Table(name = "users")
class User(

    @Id
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @UuidGenerator
    var id: UUID? = null,

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    var role: UserRole? = null,

    @Column(name = "name")
    var name: String? = null,

    @Column(name = "login")
    var login: String? = null,

    @JvmField
    @Column(name = "password")
    var password: String? = null,

    @Column(name = "created_at")
    private var createdAt: OffsetDateTime? = null,

    @Column(name = "updated_at")
    private var updatedAt: OffsetDateTime? = null
) : UserDetails {

    @PrePersist
    @PreUpdate
    fun onSaveHook() {
        role = if (this.role == null) UserRole.USER else this.role
        createdAt = if (createdAt == null) OffsetDateTime.now() else createdAt
        updatedAt = OffsetDateTime.now()
    }

    override fun getAuthorities(): List<GrantedAuthority> {
        return listOf(role!!)
    }

    override fun getPassword(): String {
        return password!!
    }

    override fun getUsername(): String {
        return login!!
    }
}
