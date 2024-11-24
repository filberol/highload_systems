package ru.itmo.oxygen.domain.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingConstants
import ru.itmo.oxygen.api.dto.OxygenSupplyResponse
import ru.itmo.oxygen.infra.model.OxygenSupply


@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    disableSubMappingMethodsGeneration = true
)
interface OxygenSupplyApiMapper {

    @Mapping(target = "oxygenStorageId", source = "oxygenStorage.id")
    fun toDto(entity: OxygenSupply): OxygenSupplyResponse

}