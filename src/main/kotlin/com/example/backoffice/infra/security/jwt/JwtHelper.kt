package com.example.backoffice.infra.security.jwt

import com.example.backoffice.infra.security.MemberRole
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*

@Component
class JwtHelper(
    @Value("\${jwt.secret}")
    private val secretKey: String,
) {
    private val key = Keys.hmacShaKeyFor(secretKey.toByteArray())

    fun generateToken(userId: Long, role: MemberRole): String {
        val issuer = "backoffice.example.com"
        val now = ZonedDateTime.now(ZoneId.of("Asia/Seoul"))

        val claims = Jwts.claims().add(mapOf("role" to role)).build()

        return Jwts
            .builder()
            .signWith(key)
            .subject(userId.toString())
            .issuer(issuer)
            .issuedAt(Date.from(now.toInstant()))
            .expiration(Date.from(now.plusHours(1).toInstant()))
            .claims(claims)
            .compact()!!
    }

    fun validateTokenAndGetClaims(token: String): Result<Jws<Claims>> {
        return runCatching {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token)
        }
    }
}