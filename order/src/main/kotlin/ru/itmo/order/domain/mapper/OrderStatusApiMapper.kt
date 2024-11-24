package ru.itmo.order.domain.mapper

import org.mapstruct.Mapper
import org.mapstruct.MappingConstants
import ru.itmo.order.api.dto.OrderStatusRequestResponse
import ru.itmo.order.infra.model.enums.OrderStatus

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    disableSubMappingMethodsGeneration = true
)
interface OrderStatusApiMapper {

    fun toResponse(entity: OrderStatus): OrderStatusRequestResponse

    fun toEntity(dto: OrderStatusRequestResponse): OrderStatus

}