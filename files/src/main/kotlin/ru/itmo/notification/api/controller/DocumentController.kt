package ru.itmo.notification.api.controller

//import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.http.MediaType
import org.springframework.http.codec.multipart.FilePart
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono
import ru.itmo.notification.api.controller.dto.FileResponse
import ru.itmo.notification.domain.service.DocumentService
import ru.itmo.notification.infra.model.Document
import java.util.*

@RestController
class DocumentController(
    private val documentService: DocumentService
) {
    @PostMapping("/files", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun upload(@RequestPart("file") file: FilePart): Mono<Document> {
        return documentService.save(file)
    }

    @GetMapping("/files/{id}")
//    @PreAuthorize("hasAnyAuthority('SUPPLIER', 'MANAGER', 'ADMIN')")
    fun download(@PathVariable id: UUID): Mono<FileResponse> {
        return documentService.getById(id)
    }

    @DeleteMapping("/files/{id}")
//    @PreAuthorize("hasAnyAuthority('SUPPLIER', 'MANAGER', 'ADMIN')")
    fun delete(@PathVariable id: UUID): Mono<Unit> {
        return documentService.deleteById(id)
    }
}