package ru.itmo.department.domain.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingConstants
import ru.itmo.department.api.dto.RoomNormResponse
import ru.itmo.department.infra.model.Room
import ru.itmo.department.infra.model.RoomNorm

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    disableSubMappingMethodsGeneration = true
)
interface RoomNormApiMapper {

    @Mapping(target = "id", source = "room.id")
    @Mapping(target = "peopleCount", source = "roomNorm.peopleCount")
    @Mapping(target = "balanceOxygen", expression = "java(room.getCapacity()-roomNorm.getSize())")
    @Mapping(target = "createdAt", source = "room.createdAt")
    @Mapping(target = "updatedAt", source = "room.updatedAt")
    @Mapping(target = "avgPersonNorm", expression = "java(roomNorm.getSize()/ roomNorm.getPeopleCount())")
    fun toResponse(room: Room, roomNorm: RoomNorm): RoomNormResponse

}
