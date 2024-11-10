package ru.itmo.highload_systems.domain.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingConstants
import ru.itmo.highload_systems.api.dto.OxygenSupplyResponse
import ru.itmo.highload_systems.infra.model.OxygenSupply


@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    disableSubMappingMethodsGeneration = true
)
interface OxygenSupplyApiMapper {

    @Mapping(target = "oxygenStorageId", source = "oxygenStorage.id")
    @Mapping(target = "departmentId", source = "department.id")
    fun toDto(entity: OxygenSupply): OxygenSupplyResponse

}