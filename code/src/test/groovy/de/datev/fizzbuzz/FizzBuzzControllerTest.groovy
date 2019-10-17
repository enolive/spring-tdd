package de.datev.fizzbuzz

import io.vavr.collection.Stream
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.test.web.reactive.server.WebTestClient
import spock.lang.Specification
import spock.lang.Unroll

@WebFluxTest(controllers = FizzBuzzController)
class FizzBuzzControllerTest extends Specification {
  @Autowired
  private WebTestClient testClient
  @SpringBean
  private FizzBuzzCalculator calculator = Mock()

  def "get single number"() {
    given: 'an input'
    int input = ObjectMother.aNumberUpTo(9000)

    when: 'GET single number is called'
    WebTestClient.ResponseSpec result = testClient.get()
                                                  .uri("/api/v1/fizz-buzz/number/$input")
                                                  .exchange()

    then: 'status is OK'
    result.expectStatus().isOk()
    and: 'content is as expected'
    result.expectBody(String).isEqualTo('result!')
    and: 'calculator is called'
    1 * calculator.single(input) >> 'result!'
  }

  def "get sequence of numbers"() {
    given: 'an upper limit'
    int limit = ObjectMother.aNumberUpTo(9000)

    when: 'GET number sequence is called'
    WebTestClient.ResponseSpec result = testClient.get()
                                                  .uri("/api/v1/fizz-buzz/numbers?limit=$limit")
                                                  .exchange()

    then: 'status is OK'
    result.expectStatus().isOk()
    and: 'content is as expected'
    result.expectBody().json('["first", "second"]')
    and: 'calculator is called'
    1 * calculator.sequence(limit) >> Stream.of("first", "second")
  }

  def "get sequence of numbers with default limit"() {
    given: 'a default limit'
    int defaultLimit = 100
    when: 'GET number sequence is called without explicitly specifying the limit'
    WebTestClient.ResponseSpec result = testClient.get()
                                                  .uri("/api/v1/fizz-buzz/numbers")
                                                  .exchange()

    then: 'status is OK'
    result.expectStatus().isOk()
    and: 'calculator is called with default limit'
    1 * calculator.sequence(defaultLimit)
  }

  @Unroll
  def "get sequence with too large limit #limit fails"() {
    when: 'GET number sequence is called'
    WebTestClient.ResponseSpec result = testClient.get()
                                                  .uri("/api/v1/fizz-buzz/numbers?limit=$limit")
                                                  .exchange()

    then: 'status is Bad Request'
    result.expectStatus().isBadRequest()
    and: 'the error message tells us that the limit must be smaller'
    result.expectBody().jsonPath('$.message').isEqualTo("limit must be less than 10000.")

    where:
    limit << [10000, 10001, 100000000]
  }
}
