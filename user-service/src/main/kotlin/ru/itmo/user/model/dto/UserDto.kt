package ru.itmo.user.model.dto

data class UserDto(
    var id: Long,
    var username: String,
    var email: String,
    var balance: Int,
    var description: String,
    var roles: Collection<String>
)
