package ru.itmo.highload_systems.domain.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingConstants
import ru.itmo.highload_systems.api.dto.OxygenStorageResponse
import ru.itmo.highload_systems.infra.model.OxygenStorage

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    disableSubMappingMethodsGeneration = true
)
interface OxygenStorageApiMapper {

    @Mapping(target = "departmentId", source = "department.ud")
    fun toDto(entity: OxygenStorage): OxygenStorageResponse

    fun toDto(entities: List<OxygenStorage>): List<OxygenStorageResponse>
}
