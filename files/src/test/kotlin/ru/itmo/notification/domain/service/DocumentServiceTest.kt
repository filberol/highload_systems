package ru.itmo.notification.domain.service

import io.minio.MinioClient
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono
import reactor.test.test
import ru.itmo.notification.api.controller.dto.FileResponse
import ru.itmo.notification.config.minio.MinioConnectionDetails
import ru.itmo.notification.infra.model.Document
import ru.itmo.notification.infra.repository.DocumentRepository
import java.util.*

class DocumentServiceTest {
    private val documentRepository = mockk<DocumentRepository>()
    private val minioConnectionDetails = mockk<MinioConnectionDetails>()
    private val minioClient = mockk<MinioClient>()
    private val documentService = DocumentService(documentRepository, minioConnectionDetails, minioClient)
    private val documentId = UUID.randomUUID()
    private val document = Document(documentId, "test-bucket", "file")

    @BeforeEach
    fun setup() {
        every { minioConnectionDetails.bucket } returns "test-bucket"
        every { minioConnectionDetails.url } returns "localhost:8080"
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
}