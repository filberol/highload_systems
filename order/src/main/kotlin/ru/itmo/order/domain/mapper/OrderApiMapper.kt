package ru.itmo.order.domain.mapper

import org.mapstruct.Mapper
import org.mapstruct.MappingConstants
import ru.itmo.order.api.dto.OrderResponse
import ru.itmo.order.infra.model.Order

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    disableSubMappingMethodsGeneration = true,
    uses = [OrderStatusApiMapper::class]
)
interface OrderApiMapper {

    fun toResponse(entity: Order): OrderResponse

}