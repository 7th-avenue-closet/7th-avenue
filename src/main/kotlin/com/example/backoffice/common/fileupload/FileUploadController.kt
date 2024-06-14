package com.example.backoffice.common.fileupload

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class FileUploadController(
    private val service: ImageUploadService
) {

    @GetMapping("/presigned")
    fun presignedUrl(@RequestParam domain: String, @RequestParam fileName: String): ResponseEntity<String> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(service.presignedUrl(domain, fileName))
    }

    @DeleteMapping("/presigned")
    fun deleteImage(@RequestParam domain: String, @RequestParam fileName: String): ResponseEntity<Unit> {
        return ResponseEntity.status(HttpStatus.OK).body(service.delete(domain, fileName))
    }
}