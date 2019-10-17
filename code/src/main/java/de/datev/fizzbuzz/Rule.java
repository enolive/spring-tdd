package de.datev.fizzbuzz;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.Predicate;

@AllArgsConstructor
class Rule {
  private Predicate<Integer> predicate;
  @Getter
  private String result;

  boolean appliesTo(int input) {
    return predicate.test(input);
  }
}
