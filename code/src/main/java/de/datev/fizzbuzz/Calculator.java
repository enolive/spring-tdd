package de.datev.fizzbuzz;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
public class Calculator {
  private final List<Rule> rules = List.of(
      new Rule(3, "Fizz"),
      new Rule(5, "Buzz")
  );

  public String singleValue(int input) {
    return tryCalculateResultFromRules(input).orElseGet(() -> String.valueOf(input));
  }

  public Stream<String> sequenceUpTo(int limit) {
    return IntStream.rangeClosed(1, limit)
                    .mapToObj(this::singleValue);
  }

  private Optional<String> tryCalculateResultFromRules(int input) {
    return Optional.of(concatMatchingResults(input))
                   .filter(Predicate.not(String::isEmpty));
  }

  private String concatMatchingResults(int input) {
    return rules.stream()
                .filter(rule -> rule.appliesTo(input))
                .map(Rule::getResult)
                .collect(Collectors.joining("-"));
  }

  @AllArgsConstructor
  private static class Rule {
    private final int divisor;
    @Getter
    private final String result;

    public boolean appliesTo(int input) {
      return input % divisor == 0;
    }
  }
}
