package ru.itmo.file.api.controller

import org.springframework.http.MediaType
import org.springframework.http.codec.multipart.FilePart
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono
import ru.itmo.file.api.controller.dto.FileResponse
import ru.itmo.file.domain.service.DocumentService
import ru.itmo.file.infra.model.Document
import java.util.*

@RestController
@RequestMapping("/files")
class DocumentController(
    private val documentService: DocumentService
) {

    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun upload(@RequestPart("file") file: Mono<FilePart>): Mono<Document> {
        return file.flatMap { f -> documentService.save(f) }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SUPPLIER', 'MANAGER', 'ADMIN')")
    fun download(@PathVariable id: UUID): Mono<FileResponse> {
        return documentService.getById(id)
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SUPPLIER', 'MANAGER', 'ADMIN')")
    fun delete(@PathVariable id: UUID): Mono<Unit> {
        return documentService.deleteById(id)
    }
}