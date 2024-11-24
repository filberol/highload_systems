package ru.itmo.department.domain.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingConstants
import ru.itmo.department.api.dto.RoomResponse
import ru.itmo.department.infra.model.Room

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    disableSubMappingMethodsGeneration = true
)
interface RoomApiMapper {

    @Mapping(target = "departmentId", source = "department.id")
    fun toResponse(entity: Room): RoomResponse
}