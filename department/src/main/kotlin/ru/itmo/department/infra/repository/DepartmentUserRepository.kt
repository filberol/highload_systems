package ru.itmo.department.infra.repository

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import ru.itmo.department.infra.model.DepartmentUser
import java.util.*

@Repository
interface DepartmentUserRepository : ReactiveCrudRepository<DepartmentUser, UUID> {
}