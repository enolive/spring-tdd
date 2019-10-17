package de.datev.fizzbuzz;

import io.vavr.collection.List;
import io.vavr.collection.Stream;
import io.vavr.control.Option;
import org.springframework.stereotype.Service;

import java.util.function.Predicate;

@Service
public class FizzBuzzCalculator {
  private final List<Rule> rules = List.of(
      new Rule(numbersDivisibleBy(3), "Fizz"),
      new Rule(numbersDivisibleBy(5), "Buzz")
  );

  private Predicate<Integer> numbersDivisibleBy(int divisor) {
    return input -> input % divisor == 0;
  }

  public Stream<String> sequence(int limit) {
    return Stream.rangeClosed(1, limit)
                 .map(this::single);
  }

  public String single(int input) {
    return getResultFromRules(input).getOrElse(() -> String.valueOf(input));
  }

  private Option<String> getResultFromRules(int input) {
    return Option.of(concatRulesThatApplyTo(input))
                 .filter(result -> !result.isEmpty());
  }

  private String concatRulesThatApplyTo(int input) {
    return rules.filter(rule -> rule.appliesTo(input))
                .map(Rule::getResult)
                .mkString("-");
  }

}
