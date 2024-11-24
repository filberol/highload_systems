package ru.itmo.oxygen.domain.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingConstants
import ru.itmo.oxygen.api.dto.OxygenStorageResponse
import ru.itmo.oxygen.infra.model.OxygenStorage

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    disableSubMappingMethodsGeneration = true
)
interface OxygenStorageApiMapper {

    fun toDto(entity: OxygenStorage): OxygenStorageResponse

}
