package com.example.backoffice.infra.security

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.security.core.authority.SimpleGrantedAuthority

@ExtendWith(MockKExtension::class)
class PreAuthorizeTest : DescribeSpec({
    describe("CustomPreAuthorize") {
        val customPreAuthorize = CustomPreAuthorize()

        describe("hasAnyRole method") {
            val validRole = "USER"
            val invalidRole = "ADMIN"
            val principalWithValidRole = MemberPrincipal(id = 5L, setOf(SimpleGrantedAuthority("ROLE_$validRole")))
            val principalWithInvalidRole = MemberPrincipal(id = 5L, setOf(SimpleGrantedAuthority("ROLE_$invalidRole")))

            context("when called with a valid role") {
                it("should execute the action") {
                    val result = customPreAuthorize.hasAnyRole(principalWithValidRole, setOf(MemberRole.USER)) {
                        "action executed"
                    }
                    result shouldBe "action executed"
                }
            }

            context("when called with an invalid role") {
                it("should throw an AccessDeniedException") {
                    shouldThrow<AccessDeniedException> {
                        customPreAuthorize.hasAnyRole(principalWithInvalidRole, setOf(MemberRole.USER)) {
                            "action executed"
                        }
                    }
                }
            }
        }
    }
})