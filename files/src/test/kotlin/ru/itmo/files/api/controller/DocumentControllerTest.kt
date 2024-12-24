package ru.itmo.files.api.controller

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.http.codec.multipart.FilePart
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import ru.itmo.files.api.controller.dto.FileResponse
import ru.itmo.files.domain.service.DocumentService
import ru.itmo.files.infra.model.Document
import java.util.*

class DocumentControllerTest {
    private val documentService = mockk<DocumentService>()
    private val sut = DocumentController(documentService)

    @Test
    fun upload_shouldInvokeServiceAndReturnResult() {
        val file = mockk<FilePart>()
        val document = Document(UUID.randomUUID(), "test", "test")
        every { documentService.save(file) } returns (Mono.just(document))
        // when & then
        StepVerifier.create(sut.upload(Mono.just(file)))
            .expectNextMatches { it.id == document.id }
            .verifyComplete()
    }

    @Test
    fun download_shouldInvokeServiceAndReturnResult() {
        val id = UUID.randomUUID()
        val response = FileResponse(id = id, "test-url")
        every { documentService.getById(id) }
            .returns(Mono.just(response))

        // when & then
        StepVerifier.create(sut.download(id))
            .expectNextMatches { it.id == id }
            .verifyComplete()
        verify { documentService.getById(id) }
    }

    @Test
    fun delete_shouldInvokeServiceAndReturnResult() {
        val id = UUID.randomUUID()
        every { documentService.deleteById(id) }
            .returns(Mono.empty())

        // when & then
        StepVerifier.create(sut.delete(id))
            .verifyComplete()
        verify { documentService.deleteById(id) }
    }
}