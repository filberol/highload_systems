package ru.itmo.file.domain.service

import io.minio.MinioClient
import io.minio.ObjectWriteResponse
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.MediaType.IMAGE_PNG_VALUE
import org.springframework.http.codec.multipart.FilePart
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import reactor.test.test
import ru.itmo.file.api.controller.dto.FileResponse
import ru.itmo.file.config.minio.MinioConnectionDetails
import ru.itmo.file.infra.model.Document
import ru.itmo.file.infra.repository.DocumentRepository
import java.util.*

class DocumentServiceTest {
    private val documentRepository = mockk<DocumentRepository>()
    private val minioConnectionDetails = mockk<MinioConnectionDetails>()
    private val minioClient = mockk<MinioClient>()
    private val documentService = DocumentService(documentRepository, minioConnectionDetails, minioClient)
    private val documentId = UUID.randomUUID()
    private val document = Document(documentId, "test-bucket", "file")
    private val httpHeaders: HttpHeaders = mockk()

    @BeforeEach
    fun setup() {
        every { minioConnectionDetails.bucket } returns "test-bucket"
        every { minioConnectionDetails.url } returns "localhost:8080"
        every { minioConnectionDetails.userUrl } returns "localhost:8080"

    }

    @Test
    fun getById_shouldReturnResponse() {
        every { documentRepository.findById(documentId) }.returns(Mono.just(document))

        val response = FileResponse(documentId, "localhost:8080/test-bucket/file")
        documentService.getById(documentId).test().expectNext(response).verifyComplete()
        verify { documentRepository.findById(documentId) }

    }

    @Test
    fun getById_shouldThrowException() {
        every { documentRepository.findById(documentId) }
            .returns(Mono.empty())

        documentService.getById(documentId).test().expectError(NoSuchElementException::class.java).verify()

        verify { documentRepository.findById(documentId) }
    }

    @Test
    fun deleteById_shouldThrowException() {
        every { documentRepository.findById(documentId) }
            .returns(Mono.empty())

        documentService.deleteById(documentId).test().expectError(NoSuchElementException::class.java).verify()

        verify { documentRepository.findById(documentId) }
    }

    @Test
    fun deleteById_shouldBeSuccessful() {
        every { documentRepository.findById(documentId) }.returns(Mono.just(document))
        every { documentRepository.delete(document) }.returns(Mono.empty())
        every { minioClient.removeObject(any()) }.returns(Unit)

        documentService.deleteById(documentId).test().expectNext(Unit).verifyComplete()

        verify { documentRepository.findById(documentId) }
        verify { documentRepository.delete(document) }
        verify { minioClient.removeObject(any()) }
    }

    @Test
    fun save_shouldBeSuccessful() {
        val filePart = mockk<FilePart>()
        every { documentRepository.existsByKey("test") }
            .returns(Mono.just(false))
        every { filePart.content() }.returns(Flux.empty())
        every { filePart.headers() }.returns(httpHeaders)
        every { filePart.filename() }.returns("test")
        every { httpHeaders.contentType } returns MediaType.parseMediaType(IMAGE_PNG_VALUE)
        every { minioClient.putObject(any()) }.returns(mockk<ObjectWriteResponse>())
        val expected = Document(UUID.randomUUID(), "test", "test")
        every { documentRepository.save(any()) }
            .returns(Mono.just(expected))

        val result = documentService.save(filePart)

        StepVerifier.create(result)
            .expectNext(expected)
            .verifyComplete()
    }

    @Test
    fun save_shouldThrowException() {
        val filePart = mockk<FilePart>()
        every { documentRepository.existsByKey("test") }
            .returns(Mono.just(true))
        every { filePart.content() }.returns(Flux.empty())
        every { filePart.headers() }.returns(httpHeaders)
        every { filePart.filename() }.returns("test")
        every { httpHeaders.contentType } returns MediaType.parseMediaType(IMAGE_PNG_VALUE)
        every { minioClient.putObject(any()) }.returns(mockk<ObjectWriteResponse>())
        val expected = Document(UUID.randomUUID(), "test", "test")
        every { documentRepository.save(any()) }
            .returns(Mono.just(expected))

        val result = documentService.save(filePart)

        StepVerifier.create(result)
            .expectError(NoSuchElementException::class.java)
            .verify()
    }
}