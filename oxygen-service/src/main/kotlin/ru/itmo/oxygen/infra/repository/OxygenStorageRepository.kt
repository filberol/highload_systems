package ru.itmo.highload_systems.infra.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import ru.itmo.highload_systems.infra.model.OxygenStorage
import java.util.*

@Repository
interface OxygenStorageRepository : JpaRepository<OxygenStorage, UUID> {
    @Query("SELECT o FROM OxygenStorage o WHERE o.department.id = :departmentId AND (o.capacity - o.size) >= :size")
    fun findByDepartmentIdAndCapacityGreaterThan(
        @Param("departmentId") departmentId: UUID,
        @Param("size") value: Long
    ): Optional<OxygenStorage>

    fun findByDepartmentId(departmentId: UUID): Optional<OxygenStorage>
}