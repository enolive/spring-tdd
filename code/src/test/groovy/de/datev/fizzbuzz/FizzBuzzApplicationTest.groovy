package de.datev.fizzbuzz

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.env.Environment
import spock.lang.Specification

@SpringBootTest
class FizzBuzzApplicationTest extends Specification {
  @Autowired
  private Environment environment

  def "context is loaded"() {
    expect: 'environment exists'
    environment != null
  }
}
