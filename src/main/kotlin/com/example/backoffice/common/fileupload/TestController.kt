package com.example.backoffice.common.fileupload

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
class TestController(
    private val service: ImageUploadService
) {

    @PostMapping("/upload")
    fun fileUpload(@RequestPart("file") file: MultipartFile): ResponseEntity<String> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(service.upload(file))
    }

    @GetMapping("/presigned")
    fun presignedUrl(@RequestParam fileName: String): ResponseEntity<String> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(service.presignedUrl(fileName))
    }

}