package ru.itmo.oxygen.infra.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import ru.itmo.oxygen.infra.model.OxygenStorage
import java.util.*

@Repository
interface OxygenStorageRepository : JpaRepository<OxygenStorage, UUID> {
    @Query("SELECT o FROM OxygenStorage o WHERE (o.size - :size) >= 0")
    fun findByCapacityGreaterThan(
        @Param("size") value: Long
    ): Optional<OxygenStorage>
}