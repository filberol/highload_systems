package ru.itmo.files.domain.service

import io.minio.MinioClient
import io.minio.PutObjectArgs
import io.minio.RemoveObjectArgs
import jakarta.persistence.EntityNotFoundException
import org.springframework.http.codec.multipart.FilePart
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import ru.itmo.files.api.controller.dto.FileResponse
import ru.itmo.files.config.MinioProperties
import ru.itmo.files.infra.model.Document
import ru.itmo.files.infra.repository.DocumentRepository
import java.util.*

@Service
class DocumentService(
    private val documentRepository: DocumentRepository,
    private val minioProperties: MinioProperties,
    private val minioClient: MinioClient
) {
    fun save(file: FilePart): Mono<Document> {
        val document = Document(
            bucket = minioProperties.bucket,
            key = file.filename(),
        )
        return file.content()
            .subscribeOn(Schedulers.boundedElastic())
            .reduce(InputStreamCollector()) { collector, dataBuffer ->
                collector.collectInputStream(dataBuffer.asInputStream())
            }
            .map { inputStreamCollector ->
                val startMillis = System.currentTimeMillis()
                val args = PutObjectArgs.builder()
                    .`object`(file.filename())
                    .contentType(file.headers().contentType!!.toString())
                    .bucket(minioProperties.bucket)
                    .stream(
                        inputStreamCollector.stream,
                        inputStreamCollector.stream.available().toLong(),
                        -1
                    )
                    .build()
                minioClient.putObject(args)
            }
            .flatMap { doc ->
                documentRepository.save(document)
            }
    }

    fun getById(id: UUID): Mono<FileResponse> {
        return documentRepository.findById(id)
            .switchIfEmpty(Mono.error(NoSuchElementException("File with id $id not found")))
            .map { doc ->
                FileResponse(
                    id,
                    "${minioProperties.endpoint}/${doc.bucket}/${doc.key}"
                )
            }
    }

    fun deleteById(id: UUID): Mono<Unit> {
        return documentRepository.findById(id)
            .switchIfEmpty(Mono.error(EntityNotFoundException("Image with ID $id not found")))
            .flatMap { obj ->
                documentRepository.deleteById(id)
                Mono.fromCallable {
                    minioClient.removeObject(
                        RemoveObjectArgs.builder()
                            .bucket(minioProperties.bucket)
                            .`object`(obj.key)
                            .build()
                    )
                }
            }
            .subscribeOn(Schedulers.boundedElastic())
            .onErrorMap { e ->
                IllegalStateException("Failed to delete image with id: $id", e)
            }
            .then(Mono.just(Unit))
    }
}