package de.datev.fizzbuzz

import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import java.security.SecureRandom
import java.util.stream.Stream

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(controllers = Controller)
class ControllerTest extends Specification {
  @Autowired
  private MockMvc mockMvc
  @SpringBean
  private Calculator calculator = Mock()

  def "single result is returned"() {
    given: "an input"
    def input = aNumber()

    when: "GET call is performed"
    def result = mockMvc.perform(get("/api/v1/fizz-buzz/numbers/$input"))

    then: "status is ok"
    result.andExpect(status().isOk())
    and: "the calculated result is returned"
    result.andExpect(content().string("Result!"))
    and: "result was calculated"
    1 * calculator.singleValue(input) >> "Result!"
  }

  def "sequence of results is returned"() {
    given: "an upper limit"
    def limit = 42
    and: "an expected json result"
    def expectedJson = """["first", "second"]"""

    when: "GET call is performed"
    def result = mockMvc.perform(get("/api/v1/fizz-buzz/numbers?limit=$limit"))

    then: "status is ok"
    result.andExpect(status().isOk())
    and: "the calculated list of results is returned"
    result.andExpect(content().json(expectedJson))
    and: "result was calculated"
    1 * calculator.sequenceUpTo(limit) >> Stream.of("first", "second")
  }

  def "sequence of results for default limit is returned"() {
    given: "an expected json result"
    def expectedJson = """["first", "second"]"""

    when: "GET call is performed"
    def result = mockMvc.perform(get("/api/v1/fizz-buzz/numbers"))

    then: "status is ok"
    result.andExpect(status().isOk())
    and: "the calculated list of results is returned"
    result.andExpect(content().json(expectedJson))
    and: "result was calculated for the default limit"
    1 * calculator.sequenceUpTo(100) >> Stream.of("first", "second")
  }

  private static int aNumber() {
    new SecureRandom().nextInt()
  }
}
