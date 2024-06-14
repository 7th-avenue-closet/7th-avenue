package com.example.backoffice.infra.security.configuration

import com.example.backoffice.infra.security.CustomAccessDeniedHandler
import com.example.backoffice.infra.security.CustomAuthenticationEntryPoint
import com.example.backoffice.infra.security.jwt.JwtAuthenticationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfig(
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
    private val authenticationEntryPoint: CustomAuthenticationEntryPoint,
    private val accessDeniedHandler: CustomAccessDeniedHandler,
) {
    val allowedUrls =
        arrayOf("/auth/sign-up", "/auth/login", "/swagger-ui/**", "/v3/**", "/error", "/admin/sign-up", "/admin/login")
    val allowedUrlsWithGetMethods = arrayOf("/products/**")

    @Bean
    fun filterChain(http: HttpSecurity): DefaultSecurityFilterChain {
        return http
            .httpBasic { it.disable() }
            .formLogin { it.disable() }
            .csrf { it.disable() }
            .authorizeHttpRequests {
                it
                    .requestMatchers(*allowedUrls)
                    .permitAll()
                    .requestMatchers(HttpMethod.GET, *allowedUrlsWithGetMethods)
                    .permitAll()
                    .anyRequest()
                    .authenticated()
            }
            .addFilterBefore(
                jwtAuthenticationFilter,
                BasicAuthenticationFilter::class.java,
            ).exceptionHandling {
                it.authenticationEntryPoint(authenticationEntryPoint)
                it.accessDeniedHandler(accessDeniedHandler)
            }
            .build()
    }
}