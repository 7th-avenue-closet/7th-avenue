package com.example.backoffice.common.fileupload

import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest
import com.amazonaws.services.s3.model.ObjectMetadata
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.util.*

@Service
class ImageUploadService(
    private val s3Client: AmazonS3Client
) {
    @Value("\${BUCKET_NAME}")
    private lateinit var bucket: String

    fun upload(file: MultipartFile): String? {
        val image = listOf("jpg", "jpeg", "png", "gif", "bmp")
        val fileName = file.originalFilename ?: throw IOException("File name is empty")
        val exception = fileName.substringAfterLast('.')
        val uploadName = LocalDateTime.now().toString() + fileName
        if (!image.contains(exception)) return null
        val metadata = ObjectMetadata().apply {
            contentType = "image/${exception}"
            contentLength = file.size
        }
        s3Client.putObject(bucket, uploadName, file.inputStream, metadata)
        return s3Client.getUrl(bucket, uploadName).toString()
    }


}