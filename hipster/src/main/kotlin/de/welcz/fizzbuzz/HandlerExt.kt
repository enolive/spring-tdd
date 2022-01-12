package de.welcz.fizzbuzz

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.queryParamOrNull

suspend fun ValidationError.responseBadRequest() = ServerResponse.badRequest().bodyValueAndAwait(this.msg)

suspend fun Any.responseOk() = ServerResponse.ok().bodyValueAndAwait(this)

fun <T> ServerRequest.extractPathParameter(paramName: String, conversionFn: (String) -> T?) =
  pathVariable(paramName)
    .let(conversionFn)
    ?.right()
    ?: ValidationError.ParamNotNumeric(paramName).left()

fun <T> ServerRequest.extractQueryParam(
  paramName: String,
  defaultValue: T,
  conversionFn: (String) -> T?,
): Either<ValidationError.ParamNotNumeric, T> {
  val limitParam = queryParamOrNull(paramName) ?: return defaultValue.right()
  return limitParam
    .let(conversionFn)
    ?.right()
    ?: ValidationError.ParamNotNumeric(paramName).left()
}

sealed class ValidationError(val msg: String) {
  class ParamNotNumeric(param: String) : ValidationError("'$param' is not a number.")
}
