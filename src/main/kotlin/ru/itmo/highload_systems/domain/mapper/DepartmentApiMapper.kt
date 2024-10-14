package ru.itmo.highload_systems.domain.mapper

import org.mapstruct.Mapper
import org.mapstruct.MappingConstants

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    disableSubMappingMethodsGeneration = true
)
class DepartmentApiMapper {
}