package de.welcz.fizzbuzz

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.env.Environment

@SpringBootTest
class ApplicationTests(private val environment: Environment) : DescribeSpec({
  describe("Application") {
    it("starts successfully") {
      environment.shouldNotBeNull()
    }
  }
})
