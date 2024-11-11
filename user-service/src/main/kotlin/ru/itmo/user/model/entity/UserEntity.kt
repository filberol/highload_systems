package ru.itmo.user.model.entity

import jakarta.persistence.*

@Entity(name = "users")
class UserEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Long? = null,

    val email: String? = null,
    val password: String? = null,
    val username: String? = null,
    var balance: Int? = null,
    val description: String? = null,

    @ManyToMany
    @JoinTable(
        name = "users_roles",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "role_id")]
    )
    val roles: MutableCollection<RoleEntity>? = null
) {
    fun addRole(role: RoleEntity) {
        roles!!.add(role)
    }

    fun removeRole(role: RoleEntity) {
        roles!!.remove(role)
    }

    fun clearRoles() {
        roles!!.clear()
    }
}



