package com.example.backoffice.common.fileupload

import com.example.backoffice.infra.security.CustomPreAuthorize
import com.example.backoffice.infra.security.MemberPrincipal
import com.example.backoffice.infra.security.MemberRole
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class FileUploadController(
    private val service: ImageUploadService,
    private val preAuthorize: CustomPreAuthorize,
) {

    @GetMapping("/presigned")
    fun presignedUrl(
        @AuthenticationPrincipal principal: MemberPrincipal,
        @RequestParam fileName: String,
        @RequestParam domain: String,
    ): ResponseEntity<String> = preAuthorize.hasAnyRole(principal, setOf(MemberRole.ADMIN)) {
        ResponseEntity
            .status(HttpStatus.CREATED)
            .body(service.presignedUrl(domain, fileName))
    }

    @DeleteMapping("/images")
    fun deleteImage(
        @AuthenticationPrincipal principal: MemberPrincipal,
        @RequestParam domain: String,
        @RequestParam fileName: String,
    ): ResponseEntity<Unit> = preAuthorize.hasAnyRole(principal, setOf(MemberRole.ADMIN)) {
        ResponseEntity.status(HttpStatus.OK).body(service.delete(domain, fileName))
    }

}