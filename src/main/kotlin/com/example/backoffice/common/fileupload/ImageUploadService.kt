package com.example.backoffice.common.fileupload

import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest
import com.amazonaws.services.s3.model.ObjectMetadata
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.time.LocalDateTime
import java.util.*

@Service
class ImageUploadService(
    private val s3Client: AmazonS3Client
) {
    @Value("\${BUCKET_NAME}")
    private lateinit var bucket: String

    fun presignedUrl(domain: String, fileName: String): String {
        val expiration = Date(System.currentTimeMillis() + 60 * 60 * 1000)
        val url = GeneratePresignedUrlRequest(bucket, "$domain/$fileName")
            .withMethod(com.amazonaws.HttpMethod.PUT)
            .withExpiration(expiration)
        return s3Client.generatePresignedUrl(url).toString()
    }


}