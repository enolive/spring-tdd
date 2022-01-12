package de.welcz.fizzbuzz

import org.springframework.stereotype.Service

@Service
class Calculator {
  private val rules = listOf(
      Rule(multiplesOf(3), "Fizz"),
      Rule(multiplesOf(5), "Buzz"),
  )

  fun single(input: Int) = rules
      .filter { it.appliesTo(input) }
      .joinToString("-") { it.result }
      .takeIf { it.isNotEmpty() }
      ?: input.toString()

  fun sequenceUpTo(limit: Int) = (1..limit).map { single(it) }

  private fun multiplesOf(divisor: Int) = { input: Int -> input % divisor == 0 }
  data class Rule(val appliesTo: (input: Int) -> Boolean, val result: String)
}
