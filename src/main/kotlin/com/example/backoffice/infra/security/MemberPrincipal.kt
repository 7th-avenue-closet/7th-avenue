package com.example.backoffice.infra.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

data class MemberPrincipal(
    val id: Long,
    val authorities: Collection<GrantedAuthority>,
) {
    constructor(
        id: Long,
        role: MemberRole,
    ) : this(id, setOf(SimpleGrantedAuthority("ROLE_$role")))
}
