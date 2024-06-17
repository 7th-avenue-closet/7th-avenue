package com.example.backoffice.infra.redis

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import java.util.concurrent.TimeUnit

@Repository
class RedisTokenRepository(
    private val redisTemplate: RedisTemplate<String, String>,
) {
    companion object {
        private const val BLACKLISTED = "blacklisted"
        private const val EXPIRY_DURATION = 3600000L
    }

    fun blacklistToken(token: String) {
        redisTemplate.opsForValue()[token, BLACKLISTED, EXPIRY_DURATION] = TimeUnit.MILLISECONDS
    }

    fun isTokenBlacklisted(token: String): Boolean {
        return redisTemplate.opsForValue()[token].toString() == BLACKLISTED
    }
}