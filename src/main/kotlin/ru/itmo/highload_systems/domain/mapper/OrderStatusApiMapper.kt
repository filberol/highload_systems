package ru.itmo.highload_systems.domain.mapper

import org.mapstruct.Mapper
import org.mapstruct.MappingConstants
import ru.itmo.highload_systems.api.dto.OrderStatusRequestResponse
import ru.itmo.highload_systems.infra.model.enums.OrderStatus

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    disableSubMappingMethodsGeneration = true
)
interface OrderStatusApiMapper {

    fun toDto(entity: OrderStatus): OrderStatusRequestResponse

    fun toEntity(dto: OrderStatusRequestResponse): OrderStatus
}