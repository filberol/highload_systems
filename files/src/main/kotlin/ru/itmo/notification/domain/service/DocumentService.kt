package ru.itmo.notification.domain.service

import io.minio.MinioClient
import io.minio.RemoveObjectArgs
import jakarta.persistence.EntityNotFoundException
import org.springframework.http.codec.multipart.FilePart
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import ru.itmo.notification.api.controller.dto.FileResponse
import ru.itmo.notification.config.MinioConnectionDetails
import ru.itmo.notification.infra.model.Document
import ru.itmo.notification.infra.repository.DocumentRepository
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
        return Mono.fromCallable { document }
       /* return file.content()
            .subscribeOn(Schedulers.boundedElastic())
            .reduce(InputStreamCollector()) { collector, dataBuffer ->
                collector.collectInputStream(dataBuffer.asInputStream())
            }
            .map { inputStreamCollector ->
                val startMillis = System.currentTimeMillis()
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
            }*/
    }

    fun getById(id: UUID): Mono<FileResponse> {
        return documentRepository.findById(id)
            .switchIfEmpty(Mono.error(NoSuchElementException("File with id $id not found")))
            .map { doc ->
                FileResponse(
                    id,
                    "${minioConnectionDetails.url}/${doc.bucket}/${doc.key}"
                )
            }
    }

    fun deleteById(id: UUID): Mono<Unit> {
        return documentRepository.findById(id)
            .switchIfEmpty(Mono.error(EntityNotFoundException("File with id $id not found")))
            .map { obj ->
                documentRepository.deleteById(id)
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