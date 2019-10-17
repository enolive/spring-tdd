package de.datev.fizzbuzz

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification
import spock.lang.Unroll

import java.util.stream.Collectors

@SpringBootTest
@ContextConfiguration(classes = FizzBuzzCalculator)
class FizzBuzzCalculatorTest extends Specification {
  @Autowired
  private FizzBuzzCalculator calculator

  def "sequence of numbers up to #limit is generated as #expected"() {
    when: 'sequence is generated'
    List<String> actual = calculator.sequence(limit).collect(Collectors.toList())

    then: 'the expected sequence is returned'
    actual == expected

    where:
    limit | expected
    0     | []
    1     | ['1']
    3     | ['1', '2', 'Fizz']
    15    | ['1', '2', 'Fizz', '4', 'Buzz', 'Fizz', '7', '8', 'Fizz', 'Buzz', '11', 'Fizz', '13', '14', 'Fizz-Buzz']
  }

  @Unroll
  def "single input #input is converted to #expected"() {
    when: 'input is converted'
    String actual = calculator.single(input)

    then: 'expected result is returned'
    actual == expected

    where:
    input | expected
    1     | '1'
    2     | '2'
    3     | 'Fizz'
    6     | 'Fizz'
    5     | 'Buzz'
    10    | 'Buzz'
    15    | 'Fizz-Buzz'
    30    | 'Fizz-Buzz'
  }
}
