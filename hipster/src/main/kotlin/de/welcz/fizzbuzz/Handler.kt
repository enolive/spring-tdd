package de.welcz.fizzbuzz

import arrow.core.continuations.either
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest

@Component
class Handler(private val calculator: Calculator) {
  suspend fun getSingle(request: ServerRequest) =
    either<RequestError, String> {
      val input = request.extractNumberFromPath("input").bind()
      calculator.single(input)
    }.foldServerResponse { it.responseOk() }

  suspend fun getSequenceUpTo(request: ServerRequest) =
    either<RequestError, List<String>> {
      val limit = request.extractNumberFromQuery("limit", 100).bind()
      calculator.sequenceUpTo(limit)
    }.foldServerResponse { it.responseOk() }
}


