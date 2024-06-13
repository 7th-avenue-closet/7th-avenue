package com.example.backoffice.infra.security.configuration

import com.example.backoffice.infra.security.CustomAuthenticationEntryPoint
import com.example.backoffice.infra.security.MemberRole
import com.example.backoffice.infra.security.jwt.JwtAuthenticationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
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
) {
    val allowedUrls =
        arrayOf("/users/auth/sign-up", "/users/auth/login", "/swagger-ui/**", "/v3/**", "/error")

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
                    .requestMatchers("/admin/**")
                    .hasRole(MemberRole.ADMIN.name)
                    .anyRequest()
                    .authenticated()
            }
            .addFilterBefore(
                jwtAuthenticationFilter,
                BasicAuthenticationFilter::class.java,
            ).exceptionHandling {
                it.authenticationEntryPoint(authenticationEntryPoint)
            }
            .build()

    }
}