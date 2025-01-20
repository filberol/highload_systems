package ru.itmo.file.domain.service

import io.minio.MinioClient
import io.minio.PutObjectArgs
import io.minio.RemoveObjectArgs
import jakarta.transaction.Transactional
import org.springframework.http.codec.multipart.FilePart
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import ru.itmo.file.api.controller.dto.FileResponse
import ru.itmo.file.config.minio.MinioConnectionDetails
import ru.itmo.file.domain.utils.InputStreamCollector
import ru.itmo.file.infra.model.Document
import ru.itmo.file.infra.repository.DocumentRepository
import java.util.*

@Service
class DocumentService(
    private val documentRepository: DocumentRepository,
    private val minioConnectionDetails: MinioConnectionDetails,
    private val minioClient: MinioClient
) {
    fun save(file: FilePart): Mono<Document> {
        val document = Document(
            bucket = minioConnectionDetails.bucket,
            key = file.filename(),
        )
        return documentRepository.existsByKey(file.filename())
            .flatMap { value ->
                if (value) return@flatMap Mono.error(NoSuchElementException("Файл с таким названием уже существует"))
                return@flatMap file.content()
                    .subscribeOn(Schedulers.boundedElastic())
                    .reduce(InputStreamCollector()) { collector, dataBuffer ->
                        collector.collectInputStream(dataBuffer.asInputStream())
                    }
                    .map { inputStreamCollector ->
                        val args = PutObjectArgs.builder()
                            .`object`(file.filename())
                            .contentType(file.headers().contentType!!.toString())
                            .bucket(minioConnectionDetails.bucket)
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
    }

    fun getById(id: UUID): Mono<FileResponse> {
        return documentRepository.findById(id)
            .switchIfEmpty(Mono.error(NoSuchElementException("Файл с id $id не найден")))
            .map { doc ->
                FileResponse(
                    id,
                    "${minioConnectionDetails.userUrl}/${doc.bucket}/${doc.key}"
                )
            }
    }

    @Transactional
    fun deleteById(id: UUID): Mono<Unit> {
        return documentRepository.findById(id)
            .switchIfEmpty(Mono.error(NoSuchElementException("Файл с id $id не найден")))
            .flatMap { obj ->
                documentRepository.delete(obj)
                    .then(
                        Mono.fromCallable {
                            minioClient.removeObject(
                                RemoveObjectArgs.builder()
                                    .bucket(minioConnectionDetails.bucket)
                                    .`object`(obj.key)
                                    .build()
                            )
                        })
            }
    }
}