package ru.itmo.department.infra.repository

import org.hibernate.query.Page
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import ru.itmo.department.infra.model.Department
import java.util.*

@Repository
interface DepartmentRepository : ReactiveCrudRepository<Department, UUID> {
}