package com.example.backoffice.infra.security

import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component

@Component
class CustomPreAuthorize {
    fun <T> hasAnyRole(principal: MemberPrincipal, validRoles: Set<MemberRole>, action: () -> T): T {
        val validAuthorities = validRoles.map { SimpleGrantedAuthority("ROLE_$it") }.toSet()
        if (principal.authorities.none { it in validAuthorities }) {
            throw AccessDeniedException("Not allowed to this API")
        }
        return action()
    }
}