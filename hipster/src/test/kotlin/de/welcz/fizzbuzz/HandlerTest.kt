package de.welcz.fizzbuzz

import com.ninjasquad.springmockk.MockkBean
import io.kotest.assertions.json.shouldMatchJson
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.verify
import org.intellij.lang.annotations.Language
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import kotlin.random.Random

@WebFluxTest
@Import(Routes::class)
@ContextConfiguration(classes = [Handler::class])
class HandlerTest(
  private val webTestClient: WebTestClient,
  @MockkBean private val calculator: Calculator,
) : DescribeSpec({
  describe("API for /fizz-buzz") {
    describe("GET /{input}") {
      it("calculates single number") {
        val input = Random.nextInt()
        every { calculator.single(input) } returns "Result!"

        val response = webTestClient.get().uri("/fizz-buzz/$input").exchange()

        response.expectStatus().isOk
        response.expectBody<String>().isEqualTo("Result!")
      }

      it("rejects wrong input") {
        val response = webTestClient.get().uri("/fizz-buzz/abcdef").exchange()

        response.expectStatus().isBadRequest
      }
    }

    describe("GET /") {
      it("calculates with specified limit") {
        val limit = Random.nextInt()
        every { calculator.sequenceUpTo(limit) } returns listOf("First", "Second")

        val response = webTestClient.get().uri("/fizz-buzz?limit=$limit").exchange()

        response.expectStatus().isOk
        response.shouldHaveJsonBody("""["First","Second"]""")
      }

      it("calculates with default limit") {
        every { calculator.sequenceUpTo(any()) } returns emptyList()

        val response = webTestClient.get().uri("/fizz-buzz").exchange()

        response.expectStatus().isOk
        verify { calculator.sequenceUpTo(100) }
      }

      it("rejects wrong limit") {
        val response = webTestClient.get().uri("/fizz-buzz?limit=abcdef").exchange()

        response.expectStatus().isBadRequest
      }
    }
  }
})

private infix fun WebTestClient.ResponseSpec.shouldHaveJsonBody(@Language("json") expectedJson: String) {
  expectBody<String>().consumeWith { it.responseBody.shouldMatchJson(expectedJson) }
}
