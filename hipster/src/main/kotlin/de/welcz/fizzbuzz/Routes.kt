package de.welcz.fizzbuzz

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class Routes(private val handler: Handler) {
  @Bean
  fun fizzBuzz() =
      coRouter {
        "/fizz-buzz".nest {
          GET("/{input}", handler::getSingle)
          GET("", handler::getSequenceUpTo)
        }
      }
}
