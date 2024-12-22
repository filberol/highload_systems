package ru.itmo.files.api.controller

import org.springframework.http.codec.multipart.FilePart
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono
import ru.itmo.files.api.controller.dto.FileResponse
import ru.itmo.files.domain.service.DocumentService
import ru.itmo.files.infra.model.Document
import java.util.*

@RestController
class DocumentController(
    private val documentService: DocumentService
) {
    @PostMapping("/upload")
    @PreAuthorize("hasAnyAuthority('SUPPLIER', 'MANAGER', 'ADMIN')")
    fun upload(@RequestParam("file") file: Mono<FilePart>): Mono<Document> {
        return file.flatMap { f -> documentService.save(f) }
    }

    @GetMapping("/download/{id}")
    @PreAuthorize("hasAnyAuthority('SUPPLIER', 'MANAGER', 'ADMIN')")
    fun download(@PathVariable id: UUID) : Mono<FileResponse>{
        return documentService.getById(id)
    }

    @GetMapping("/download/{id}")
    @PreAuthorize("hasAnyAuthority('SUPPLIER', 'MANAGER', 'ADMIN')")
    fun delete(id: UUID) : Mono<Unit> {
        return documentService.deleteById(id)
    }
}