package de.datev.fizzbuzz

import io.vavr.collection.Stream
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import spock.lang.Specification
import spock.lang.Unroll

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SuppressWarnings("GroovyAssignabilityCheck")
@WebMvcTest(controllers = FizzBuzzController)
class FizzBuzzControllerTest extends Specification {
  @Autowired
  private MockMvc mockMvc
  @SpringBean
  private FizzBuzzCalculator calculator = Mock()

  def "get single number"() {
    given: 'an input'
    int input = ObjectMother.aNumberUpTo(9000)

    when: 'GET single number is called'
    ResultActions result = mockMvc.perform(get("/api/v1/fizz-buzz/number/$input"))

    then: 'status is OK'
    result.andExpect(status().isOk())
    and: 'content is as expected'
    result.andExpect(content().string('result!'))
    and: 'calculator is called'
    1 * calculator.single(input) >> 'result!'
  }

  def "get sequence of numbers"() {
    given: 'an upper limit'
    int limit = ObjectMother.aNumberUpTo(9000)

    when: 'GET number sequence is called'
    ResultActions result = mockMvc.perform(get("/api/v1/fizz-buzz/numbers?limit=$limit"))

    then: 'status is OK'
    result.andExpect(status().isOk())
    and: 'content is as expected'
    result.andExpect(content().json('["first", "second"]'))
    and: 'calculator is called'
    1 * calculator.sequence(limit) >> Stream.of("first", "second")
  }

  def "get sequence of numbers with default limit"() {
    given: 'a default limit'
    int defaultLimit = 100
    when: 'GET number sequence is called without explicitly specifying the limit'
    ResultActions result = mockMvc.perform(get("/api/v1/fizz-buzz/numbers"))

    then: 'status is OK'
    result.andExpect(status().isOk())
    and: 'calculator is called with default limit'
    1 * calculator.sequence(defaultLimit)
  }

  @Unroll
  def "get sequence with too large limit #limit fails"() {
    when: 'GET number sequence is called'
    ResultActions result = mockMvc.perform(get("/api/v1/fizz-buzz/numbers?limit=$limit"))

    then: 'status is Bad Request'
    result.andExpect(status().isBadRequest())
    and: 'the error message tells us that the limit must be smaller'
    result.andExpect(status().reason("limit must be less than 10000."))

    where:
    limit << [10000, 10001, 100000000]
  }
}
