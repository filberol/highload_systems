package ru.itmo.highload_systems.domain.mapper

import org.mapstruct.Mapper
import org.mapstruct.MappingConstants
import org.springframework.data.domain.Page
import ru.itmo.highload_systems.api.dto.DepartmentResponse
import ru.itmo.highload_systems.infra.model.Department

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    disableSubMappingMethodsGeneration = true
)
interface DepartmentApiMapper {

    fun toDto(entity: Department): DepartmentResponse

    fun toDto(entities: Page<Department>): Page<DepartmentResponse>
}