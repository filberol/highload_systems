package ru.itmo.highload_systems.domain.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingConstants
import ru.itmo.highload_systems.api.dto.OrderResponse
import ru.itmo.highload_systems.infra.model.Order

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    disableSubMappingMethodsGeneration = true,
    uses = [OrderStatusApiMapper::class]
)
interface OrderApiMapper {

    @Mapping(target = "departmentId", source = "department.id")
    fun toDto(entity: Order): OrderResponse

    fun toDto(entities: List<Order>): List<OrderResponse>
}