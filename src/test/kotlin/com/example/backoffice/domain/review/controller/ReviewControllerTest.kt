package com.example.backoffice.domain.review.controller

import com.example.backoffice.common.fileupload.ImageUploadService
import com.example.backoffice.common.fileupload.S3.S3Config
import com.example.backoffice.domain.product.review.dto.ReviewRequest
import com.example.backoffice.domain.product.review.service.ReviewService
import com.example.backoffice.infra.security.MemberRole
import com.example.backoffice.infra.security.jwt.JwtHelper
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.matchers.shouldBe
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import org.mockito.Mockito.doNothing
import org.springframework.http.HttpStatus

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockKExtension::class)
class ReviewControllerTest @Autowired constructor(
    private val mockMvc: MockMvc,
    private val jwtHelper: JwtHelper,

    @MockBean
    private val reviewService: ReviewService,
    @MockBean
    private val imageUploadService : ImageUploadService,
    @MockBean
    private val s3Config: S3Config
) : DescribeSpec({
    extensions(SpringExtension)

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

                // every { reviewService.createReview(principalId, productId, reviewRequest) } just Runs
                doNothing().`when`(reviewService).createReview(principalId, productId, reviewRequest)
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
})