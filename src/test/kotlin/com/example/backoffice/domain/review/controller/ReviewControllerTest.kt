package com.example.backoffice.domain.review.controller

import com.example.backoffice.common.ZonedDateTimeDeserializer
import com.example.backoffice.common.exception.dto.ErrorResponse
import com.example.backoffice.domain.product.model.Product
import com.example.backoffice.domain.product.review.dto.ReviewRequest
import com.example.backoffice.domain.product.review.dto.ReviewResponse
import com.example.backoffice.domain.product.review.dto.toResponse
import com.example.backoffice.domain.product.review.model.Rating
import com.example.backoffice.domain.product.review.model.Review
import com.example.backoffice.domain.product.review.service.ReviewService
import com.example.backoffice.domain.user.model.User
import com.example.backoffice.infra.security.MemberRole
import com.example.backoffice.infra.security.jwt.JwtHelper
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.mockk.Runs
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.just
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.util.LinkedMultiValueMap
import java.time.ZonedDateTime

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockKExtension::class)
class ReviewControllerTest @Autowired constructor(
    private val mockMvc: MockMvc,
    private val jwtHelper: JwtHelper,

    @MockkBean
    private val reviewService: ReviewService,
) : DescribeSpec({
    extensions(SpringExtension)

    val objectMapper = jacksonObjectMapper().apply {
        registerModule(JavaTimeModule().apply {
            addDeserializer(
                ZonedDateTime::class.java,
                ZonedDateTimeDeserializer()
            )
        })
    }

    describe("POST /products/{productId}/reviews") {
        context("PRODUCT 에 새로운 REVIEW 를 작성하면") {
            it("201 status code 응답한다.") {

                val productId = 1L
                val principalId = 123L
                val reviewRequest = ReviewRequest(
                    comment = "품질이나, 가격 측면에서 너무 좋습니다.",
                    rating = "FIVE",
                    imageUrl = ""
                )

                every { reviewService.createReview(principalId, productId, reviewRequest) } just Runs
//                doNothing().`when`(reviewService).createReview(principalId, productId, reviewRequest)
                val jwtToken = jwtHelper.generateToken(principalId, MemberRole.USER)

                val result = mockMvc.perform(
                    post("/products/$productId/reviews")
                        .header("Authorization", "Bearer $jwtToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ObjectMapper().writeValueAsString(reviewRequest))
                ).andReturn()

                result.response.status shouldBe HttpStatus.CREATED.value()
            }
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


        context("john tries to get reviews") {
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
                    every { reviewService.getReviews(userId = null) } returns reviewResponses
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
                    every { reviewService.getReviews(john.id!!) } returns johnsReviewResponses

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
        val adminId = 1L
        val adminToken = jwtHelper.generateToken(adminId, MemberRole.ADMIN)
        val reviewIds = listOf(1L, 2L)

        every { reviewService.deleteReviewsByAdmin(reviewIds) } returns Unit

        it("should return NO_CONTENT status") {
            val result = mockMvc.perform(
                delete("/admin/reviews")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer $adminToken")
                    .content(objectMapper.writeValueAsString(reviewIds))
            ).andReturn()

            result.response.status shouldBe 204
        }
    }
})