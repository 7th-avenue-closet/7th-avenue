package com.example.backoffice.common.fileupload

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class TestController(
    private val service: ImageUploadService
) {

    @GetMapping("/presigned")
    fun presignedUrl(@RequestParam fileName: String, @RequestParam domain: String): ResponseEntity<String> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(service.presignedUrl(domain, fileName))
    }

}