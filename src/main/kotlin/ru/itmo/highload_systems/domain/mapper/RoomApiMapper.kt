package ru.itmo.highload_systems.domain.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingConstants
import org.springframework.data.domain.Page
import ru.itmo.highload_systems.api.dto.RoomResponse
import ru.itmo.highload_systems.infra.model.Room

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    disableSubMappingMethodsGeneration = true
)
interface RoomApiMapper {

    @Mapping(target = "size", source = "roomNorm.size")
    @Mapping(target = "avgPersonNorm", source = "roomNorm.avgPersonNorm")
    fun toDto(entity: Room): RoomResponse


    fun toDto(entities: Page<Room>): Page<RoomResponse>
}