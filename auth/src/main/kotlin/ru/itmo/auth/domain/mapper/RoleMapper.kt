package ru.itmo.auth.domain.mapper

import org.mapstruct.Mapper
import org.mapstruct.MappingConstants
import ru.itmo.auth.api.dto.RoleRequestResponse
import ru.itmo.auth.infra.model.enums.UserRole

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    disableSubMappingMethodsGeneration = true
)
interface RoleMapper {

    fun toResponse(entity: UserRole): RoleRequestResponse

    fun toEntity(dto: RoleRequestResponse): UserRole

}