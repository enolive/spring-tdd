package de.welcz.fizzbuzz

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import mu.KotlinLogging
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.queryParamOrNull

private val log = KotlinLogging.logger { }

suspend fun RequestError.responseError(): ServerResponse {
  log.warn { this }
  return when (this) {
    is RequestError.ParamNotNumeric -> ServerResponse.badRequest().bodyValueAndAwait(this.msg)
  }
}

suspend fun Any.responseOk() = ServerResponse.ok().bodyValueAndAwait(this)

fun ServerRequest.extractNumberFromPath(paramName: String) =
  pathVariable(paramName)
    .toIntOrNull()
    ?.right()
    ?: RequestError.ParamNotNumeric(paramName).left()

fun ServerRequest.extractNumberFromQuery(
  paramName: String,
  defaultValue: Int,
): Either<RequestError.ParamNotNumeric, Int> {
  val limitParam = queryParamOrNull(paramName) ?: return defaultValue.right()
  return limitParam
    .toIntOrNull()
    ?.right()
    ?: RequestError.ParamNotNumeric(paramName).left()
}

sealed class RequestError(val msg: String) {
  data class ParamNotNumeric(val param: String) : RequestError("'$param' is not a number.")
}

suspend fun <T> Either<RequestError, T>.foldServerResponse(f: suspend (T) -> ServerResponse) =
  fold({ it.responseError() }, { f(it) })
