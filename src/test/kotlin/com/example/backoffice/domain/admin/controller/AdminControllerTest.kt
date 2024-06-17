package com.example.backoffice.domain.admin.controller

import com.example.backoffice.common.ZonedDateTimeDeserializer
import com.example.backoffice.common.exception.dto.ErrorResponse
import com.example.backoffice.domain.admin.dto.AdminSignUpRequest
import com.example.backoffice.domain.admin.dto.AdminSignUpResponse
import com.example.backoffice.domain.admin.service.AdminService
import com.example.backoffice.domain.product.model.Product
import com.example.backoffice.domain.product.review.dto.ReviewResponse
import com.example.backoffice.domain.product.review.dto.toResponse
import com.example.backoffice.domain.product.review.model.Rating
import com.example.backoffice.domain.product.review.model.Review
import com.example.backoffice.domain.user.dto.LoginRequest
import com.example.backoffice.domain.user.dto.LoginResponse
import com.example.backoffice.domain.user.model.User
import com.example.backoffice.infra.security.CustomPreAuthorize
import com.example.backoffice.infra.security.MemberPrincipal
import com.example.backoffice.infra.security.MemberRole
import com.example.backoffice.infra.security.jwt.JwtHelper
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.util.LinkedMultiValueMap
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockKExtension::class)
class AdminControllerTest @Autowired constructor(
    private val mockMvc: MockMvc,
    @MockkBean
    private val adminService: AdminService,
    private val jwtHelper: JwtHelper,
) : DescribeSpec({
    extension(SpringExtension)
    afterContainer {
        clearAllMocks()
    }
    val preAuthorize = CustomPreAuthorize()
    val adminController = AdminController(adminService, preAuthorize)


    val objectMapper = jacksonObjectMapper().apply {
        registerModule(JavaTimeModule().apply {
            addSerializer(
                ZonedDateTime::class.java,
                ZonedDateTimeSerializer(DateTimeFormatter.ISO_ZONED_DATE_TIME.withZone(ZoneId.of("Asia/Seoul")))
            )
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

        describe("getReviews") {
            val john = User.of("test123", password = "test1234", name = "John").apply { id = 1L }

            val testProduct = Product.of(
                "test_product", 2000L, description = "test_description",
                category = "ACC",
                stock = 100,
                discountRate = 0,
                imageUrl = null
            ).apply { id = 5L }

            val reviewWrittenByJohn = Review.of(
                comment = "TEST",
                rating = Rating.ONE,
                user = john,
                product = testProduct,
                imageUrl = null
            ).apply { id = 10L }

            val jane = User.of("test1234", password = "test1234", name = "Jane").apply { id = 2L }

            val reviewWrittenByJane = Review.of(
                comment = "Good",
                rating = Rating.FIVE,
                user = jane,
                product = testProduct,
                imageUrl = null
            ).apply { id = 15L }


            describe("john tries to get reviews") {
                it("should return FORBIDDEN status with message Not allowed to this API") {
                    val johnToken = jwtHelper.generateToken(john.id!!, MemberRole.USER)
                    val params = LinkedMultiValueMap<String, String>()
                    params.add("userId", john.id!!.toString())
                    val result = mockMvc.perform(
                        get("/admin/reviews")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer $johnToken")
                            .params(params)
                    ).andReturn()

                    result.response.status shouldBe 403
                    val errorResponseDto = objectMapper.readValue(
                        result.response.contentAsString,
                        ErrorResponse::class.java,
                    )
                    errorResponseDto.message shouldBe "Not allowed to this API"
                }
            }

            describe("admin tries to get reviews") {
                val adminId = 1L

                val adminToken = jwtHelper.generateToken(adminId, MemberRole.ADMIN)


                context("without userId") {
                    it("should return OK status and List of ReviewResponse") {
                        val reviewResponses = listOf(reviewWrittenByJohn, reviewWrittenByJane).map { it.toResponse() }
                        every { adminService.getReviews(userId = null) } returns reviewResponses
                        val result = mockMvc.perform(
                            get("/admin/reviews")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer $adminToken")
                        ).andReturn()


                        result.response.status shouldBe 200
                        val getReviewsDto = objectMapper.readValue(
                            result.response.contentAsString,
                            object : TypeReference<List<ReviewResponse>>() {}
                        )
                        getReviewsDto shouldBe reviewResponses
                    }
                }

                context("with john's id") {

                    it("should return OK status and List of John's Reviews Response") {
                        val johnsReviewResponses = listOf(reviewWrittenByJohn.toResponse())
                        every { adminService.getReviews(john.id!!) } returns johnsReviewResponses

                        val params = LinkedMultiValueMap<String, String>()
                        params.add("userId", john.id!!.toString())
                        val result = mockMvc.perform(
                            get("/admin/reviews")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer $adminToken")
                                .params(params)
                        ).andReturn()

                        result.response.status shouldBe 200
                        val getReviewsDto = objectMapper.readValue(
                            result.response.contentAsString,
                            object : TypeReference<List<ReviewResponse>>() {}
                        )
                        getReviewsDto should { it.all { review -> review.userId == john.id } }
                    }
                }
            }
        }

        describe("deleteReviews") {
            val principal = MemberPrincipal(1L, setOf(SimpleGrantedAuthority("ROLE_${MemberRole.ADMIN}")))
            val reviewIds = listOf(1L, 2L)

            every { adminService.deleteReviews(reviewIds) } returns Unit

            it("should return NO_CONTENT status") {
                val response = adminController.deleteReviews(principal, reviewIds)
                response.statusCode shouldBe HttpStatus.NO_CONTENT
                verify { adminService.deleteReviews(reviewIds) }
            }
        }
    }
})
