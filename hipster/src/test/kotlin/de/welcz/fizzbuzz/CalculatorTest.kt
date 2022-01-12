package de.welcz.fizzbuzz

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe

class CalculatorTest : DescribeSpec({
  val sut = Calculator()

  describe("calculation") {
    describe("calculates single number") {
      data class TestCase(val input: Int, val expected: String)
      withData(
        nameFn = { "${it.input} to ${it.expected}" },
        TestCase(1, "1"),
        TestCase(2, "2"),
        TestCase(3, "Fizz"),
        TestCase(6, "Fizz"),
        TestCase(9, "Fizz"),
        TestCase(5, "Buzz"),
        TestCase(10, "Buzz"),
        TestCase(15, "Fizz-Buzz"),
        TestCase(30, "Fizz-Buzz"),
      ) { (input, expected) ->
        sut.single(input) shouldBe expected
      }
    }

    describe("calculates sequence") {
      data class TestCase(val limit: Int, val expected: List<String>)
      withData(
        nameFn = { "up to ${it.limit}" },
        TestCase(0, emptyList()),
        TestCase(1, listOf("1")),
        TestCase(3, listOf("1", "2", "Fizz")),
        TestCase(15,
          listOf("1", "2", "Fizz", "4", "Buzz", "Fizz", "7", "8", "Fizz", "Buzz", "11", "Fizz", "13", "14", "Fizz-Buzz")),
      ) { (input, expected) ->
        sut.sequenceUpTo(input) shouldBe expected
      }
    }
  }
})
