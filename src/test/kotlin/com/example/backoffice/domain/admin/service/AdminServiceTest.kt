import com.example.backoffice.domain.admin.dto.AdminSignUpRequest
import com.example.backoffice.domain.admin.dto.AdminSignUpResponse
import com.example.backoffice.domain.admin.model.Admin
import com.example.backoffice.domain.admin.repository.AdminRepository
import com.example.backoffice.domain.admin.service.AdminService
import com.example.backoffice.domain.product.review.repository.ReviewRepository
import com.example.backoffice.domain.user.dto.LoginRequest
import com.example.backoffice.domain.user.dto.LoginResponse
import com.example.backoffice.domain.user.repository.UserRepository
import com.example.backoffice.infra.security.MemberRole
import com.example.backoffice.infra.security.jwt.JwtHelper
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.password.PasswordEncoder

@SpringBootTest
@ExtendWith(MockKExtension::class)
class AdminServiceTest : DescribeSpec({
    extension(SpringExtension)

    afterContainer {
        clearAllMocks()
    }

    val adminRepository = mockk<AdminRepository>()
    val passwordEncoder = mockk<PasswordEncoder>()
    val jwtHelper = mockk<JwtHelper>()
    val reviewRepository = mockk<ReviewRepository>()
    val userRepository = mockk<UserRepository>()

    val adminService = AdminService(adminRepository, passwordEncoder, jwtHelper, reviewRepository, userRepository)

    describe("AdminService") {

        describe("admin sign-up") {
            val accountId = "admin"
            val password = "password"
            val encodedPassword = "encodedPassword"
            val adminSignUpRequest = AdminSignUpRequest(accountId, password)
            val admin = Admin.of(accountId, encodedPassword)

            context("when admin with the same accountId does not exist") {
                every { adminRepository.existsByAccountId(accountId) } returns false
                every { passwordEncoder.encode(password) } returns encodedPassword
                every { adminRepository.save(any<Admin>()) } returns admin.apply { id = 1L }

                it("should create and return AdminSignUpResponse") {
                    val response = adminService.createAdmin(adminSignUpRequest)
                    response shouldBe AdminSignUpResponse(id = 1L)
                }
            }

            context("when admin with the same accountId already exists") {
                every { adminRepository.existsByAccountId(accountId) } returns true

                it("should throw IllegalArgumentException") {
                    shouldThrow<IllegalArgumentException> {
                        adminService.createAdmin(adminSignUpRequest)
                    }
                }
            }
        }

        describe("login") {
            val accountId = "admin"
            val password = "password"
            val encodedPassword = "encodedPassword"
            val admin = Admin.of(accountId, encodedPassword).apply { id = 1L }
            val loginRequest = LoginRequest(accountId, password)
            val token = "token"

            context("when credentials are valid") {
                every { adminRepository.findByAccountId(accountId) } returns admin
                every { passwordEncoder.matches(password, encodedPassword) } returns true
                every { jwtHelper.generateToken(admin.id!!, MemberRole.ADMIN) } returns token

                it("should return LoginResponse with token") {
                    val response = adminService.login(loginRequest)
                    response shouldBe LoginResponse(accessToken = token)
                }
            }

            context("when credentials are invalid") {
                every { adminRepository.findByAccountId(accountId) } returns admin
                every { passwordEncoder.matches(password, encodedPassword) } returns false

                it("should throw IllegalArgumentException") {
                    shouldThrow<IllegalArgumentException> {
                        adminService.login(loginRequest)
                    }
                }
            }

            context("when admin is not found") {
                every { adminRepository.findByAccountId(accountId) } returns null

                it("should throw IllegalArgumentException") {
                    shouldThrow<IllegalArgumentException> {
                        adminService.login(loginRequest)
                    }
                }
            }
        }
    }
})
