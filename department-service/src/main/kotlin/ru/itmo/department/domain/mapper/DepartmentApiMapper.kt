package ru.itmo.department.domain.mapper

import org.mapstruct.Mapper
import org.mapstruct.MappingConstants
import ru.itmo.department.api.dto.DepartmentResponse
import ru.itmo.department.infra.model.Department

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    disableSubMappingMethodsGeneration = true
)
interface DepartmentApiMapper {

    fun toResponse(entity: Department): DepartmentResponse

}