package de.welcz.fizzbuzz.helper

import io.kotest.assertions.json.shouldMatchJson
import org.intellij.lang.annotations.Language
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody

infix fun WebTestClient.ResponseSpec.shouldHaveJsonBody(@Language("json") expectedJson: String) {
  expectBody<String>().consumeWith { it.responseBody.shouldMatchJson(expectedJson) }
}
