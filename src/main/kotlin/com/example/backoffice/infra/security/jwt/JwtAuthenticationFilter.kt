package com.example.backoffice.infra.security.jwt

import com.example.backoffice.infra.security.MemberPrincipal
import com.example.backoffice.infra.security.MemberRole
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(private val jwtHelper: JwtHelper) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val token = request.getBearerToken()
        if (token != null) {
            jwtHelper.validateTokenAndGetClaims(token).onSuccess { claims ->
                val id = claims.payload.subject.toLong()
                val role = MemberRole.valueOf(claims.payload["role"] as String)

                val authentication = JwtAuthenticationToken(
                    principal = MemberPrincipal(
                        id = id,
                        role = role,
                    ),
                    details = WebAuthenticationDetailsSource().buildDetails(request)
                )

                SecurityContextHolder.getContext().authentication = authentication
            }
        }

        filterChain.doFilter(request, response)
    }

    private fun HttpServletRequest.getBearerToken(): String? {
        val header = this.getHeader(HttpHeaders.AUTHORIZATION) ?: return null
        val prefix = "Bearer "
        return if (header.startsWith(prefix, ignoreCase = true))
            header.substring(prefix.length)
        else null
    }
}