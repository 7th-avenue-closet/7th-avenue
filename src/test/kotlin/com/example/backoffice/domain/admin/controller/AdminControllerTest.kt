package com.example.backoffice.domain.admin.controller

import com.example.backoffice.common.ZonedDateTimeDeserializer
import com.example.backoffice.domain.admin.dto.AdminSignUpRequest
import com.example.backoffice.domain.admin.dto.AdminSignUpResponse
import com.example.backoffice.domain.admin.service.AdminService
import com.example.backoffice.domain.user.dto.LoginRequest
import com.example.backoffice.domain.user.dto.LoginResponse
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import java.time.ZonedDateTime


@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockKExtension::class)
class AdminControllerTest @Autowired constructor(
    private val mockMvc: MockMvc,
    @MockkBean
    private val adminService: AdminService,
) : DescribeSpec({
    extension(SpringExtension)
    afterContainer {
        clearAllMocks()
    }

    val objectMapper = jacksonObjectMapper().apply {
        registerModule(JavaTimeModule().apply {
            addDeserializer(
                ZonedDateTime::class.java,
                ZonedDateTimeDeserializer()
            )
        })

    }

    describe("AdminController") {

        describe("adminSignUp") {
            val adminSignUpRequest = AdminSignUpRequest("admin", "password")
            val adminSignUpResponse = AdminSignUpResponse(1L)
            every { adminService.createAdmin(adminSignUpRequest) } returns adminSignUpResponse

            it("should return CREATED status and AdminSignUpResponse") {
                val result = mockMvc.perform(
                    post("/admin/sign-up")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                            objectMapper.writeValueAsString(
                                adminSignUpRequest,
                            ),
                        ),
                ).andReturn()

                result.response.status shouldBe 201
                val signUpResponseDto = objectMapper.readValue(
                    result.response.contentAsString,
                    adminSignUpResponse::class.java,
                )
                signUpResponseDto shouldBe adminSignUpResponse
            }
        }

        describe("login") {
            val loginRequest = LoginRequest("admin", "password")
            val loginResponse = LoginResponse("some token")
            every { adminService.login(loginRequest) } returns loginResponse

            it("should return CREATED status and LoginResponse") {
                val result = mockMvc.perform(
                    post("/admin/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                            objectMapper.writeValueAsString(
                                loginRequest,
                            ),
                        ),
                ).andReturn()

                result.response.status shouldBe 201
                val loginResponseDto = objectMapper.readValue(
                    result.response.contentAsString,
                    loginResponse::class.java,
                )
                loginResponseDto shouldBe loginResponse
            }
        }
    }
})
