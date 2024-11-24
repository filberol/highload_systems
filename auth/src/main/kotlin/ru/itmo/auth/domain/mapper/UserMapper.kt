package ru.itmo.auth.domain.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingConstants
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import ru.itmo.auth.api.dto.CreateUserRequest
import ru.itmo.auth.api.dto.RegisterRequest
import ru.itmo.auth.api.dto.UserResponse
import ru.itmo.auth.infra.auth.UserDetails
import ru.itmo.auth.infra.model.User

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    disableSubMappingMethodsGeneration = true,
    uses = [RoleMapper::class]
)
abstract class UserMapper {
    @Autowired
    protected lateinit var passwordEncoder: PasswordEncoder


    @Mapping(
        target = "password",
        expression = "java(passwordEncoder.encode(request.getPassword()))"
    )
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    abstract fun toEntity(request: RegisterRequest): User

    @Mapping(
        target = "password",
        expression = "java(passwordEncoder.encode(request.getPassword()))"
    )
    @Mapping(target = "id", ignore = true)
    abstract fun toEntity(request: CreateUserRequest): User

    abstract fun toResponse(entity: User): UserResponse

    abstract fun toDetails(userResponse: UserResponse): UserDetails
}