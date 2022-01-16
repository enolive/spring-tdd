package de.welcz.fizzbuzz

import io.kotest.core.spec.style.DescribeSpec
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import org.springframework.web.reactive.function.server.coRouter
import kotlin.random.Random

@WebFluxTest
@Import(TestRoutes::class)
class HandlerExtTest(private val webTestClient: WebTestClient) : DescribeSpec({
  describe("extensions for handlers") {
    describe("creating responses") {
      it("produces a Bad Request") {
        val response = webTestClient.get().uri("/bad-request").exchange()

        response.expectStatus().isBadRequest
        response.expectBody<String>().isEqualTo("'foo' is not a number.")
      }

      it("produces an OK") {
        val response = webTestClient.get().uri("/ok").exchange()

        response.expectStatus().isOk
        response.expectBody<String>().isEqualTo("42")
      }
    }

    describe("extracting stuff from the request") {
      describe("number from path") {
        it("succeeds") {
          val input = Random.nextInt()

          val response = webTestClient.get().uri("/number-from-path/$input").exchange()

          response.expectStatus().isOk
          response.expectBody<String>().isEqualTo(input.toString())
        }
        it("fails on non numeric param") {
          val input = "not a number"

          val response = webTestClient.get().uri("/number-from-path/$input").exchange()

          response.expectStatus().isBadRequest
          response.expectBody<String>().isEqualTo("'input' is not a number.")
        }
      }

      describe("number from query") {
        it("succeeds") {
          val input = Random.nextInt()

          val response = webTestClient.get().uri("/number-from-query?param=$input").exchange()

          response.expectStatus().isOk
          response.expectBody<String>().isEqualTo(input.toString())
        }
        it("falls back to default on missing param") {
          val response = webTestClient.get().uri("/number-from-query").exchange()

          response.expectStatus().isOk
          response.expectBody<String>().isEqualTo("100")
        }
        it("fails on non numeric param") {
          val input = "not a number"

          val response = webTestClient.get().uri("/number-from-query?param=$input").exchange()

          response.expectStatus().isBadRequest
          response.expectBody<String>().isEqualTo("'param' is not a number.")
        }
      }
    }
  }

})

class TestRoutes {
  @Bean
  fun routes() = coRouter {
    GET("/ok") {
      42.responseOk()
    }
    GET("/bad-request") {
      RequestError.ParamNotNumeric("foo").responseBadRequest()
    }
    GET("/number-from-path/{input}") {
      it.extractNumberFromPath("input")
        .fold(
          { error -> error.responseBadRequest() },
          { param -> param.responseOk() }
        )
    }
    GET("/number-from-query") {
      it.extractNumberFromQuery("param", 100)
        .fold(
          { error -> error.responseBadRequest() },
          { param -> param.responseOk() }
        )
    }
  }
}
