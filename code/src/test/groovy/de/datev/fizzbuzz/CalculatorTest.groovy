package de.datev.fizzbuzz

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import static org.assertj.core.api.Assertions.assertThat

@SpringBootTest
@ContextConfiguration(classes = Calculator)
class CalculatorTest extends Specification {
  @Autowired
  private Calculator sut

  def "single value #input is converted to #expected due to #rule"() {
    when: "single value is requested"
    def result = sut.singleValue(input)

    then: "the expected value is returned"
    result == expected

    where:
    input | rule                                                           || expected
    1     | "replication"                                                  || "1"
    2     | "replication"                                                  || "2"
    3     | "numbers divisible by 3 are converted to Fizz"                 || "Fizz"
    6     | "numbers divisible by 3 are converted to Fizz"                 || "Fizz"
    9     | "numbers divisible by 3 are converted to Fizz"                 || "Fizz"
    5     | "numbers divisible by 5 are converted to Buzz"                 || "Buzz"
    10    | "numbers divisible by 5 are converted to Buzz"                 || "Buzz"
    20    | "numbers divisible by 5 are converted to Buzz"                 || "Buzz"
    15    | "numbers divisible by both 3 and 5 are converted to Fizz-Buzz" || "Fizz-Buzz"
    30    | "numbers divisible by both 3 and 5 are converted to Fizz-Buzz" || "Fizz-Buzz"
    45    | "numbers divisible by both 3 and 5 are converted to Fizz-Buzz" || "Fizz-Buzz"
  }

  def "sequence up to #limit is converted to #expected"() {
    when: "sequence is requested"
    def result = sut.sequenceUpTo(limit)

    then: "the expected sequence is returned"
    assertThat(result).isEqualTo(expected)

    where:
    limit || expected
    0     || []
    1     || ["1"]
    3     || ["1", "2", "Fizz"]
    15    || ["1", "2", "Fizz", "4", "Buzz", "Fizz", "7", "8", "Fizz", "Buzz", "11", "Fizz", "13", "14", "Fizz-Buzz"]
  }
}
