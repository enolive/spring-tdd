package de.welcz.fizzbuzz

import arrow.core.computations.either
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest

@Component
class Handler(private val calculator: Calculator) {
  suspend fun getSingle(request: ServerRequest) =
    either<ValidationError, String> {
      val input = request.extractPathParameter("input") { it.toIntOrNull() }.bind()
      calculator.single(input)
    }.fold(
      { it.responseBadRequest() },
      { it.responseOk() }
    )

  suspend fun getSequenceUpTo(request: ServerRequest) =
    either<ValidationError, List<String>> {
      val limit = request.extractQueryParam("limit", 100) { it.toIntOrNull() }.bind()
      calculator.sequenceUpTo(limit)
    }.fold(
      { it.responseBadRequest() },
      { it.responseOk() }
    )
}


