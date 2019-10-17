package de.datev.fizzbuzz;

import io.vavr.collection.Stream;
import io.vavr.control.Either;
import io.vavr.control.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

import static java.util.function.Function.identity;

@RestController
@RequestMapping("/api/v1/fizz-buzz")
public class FizzBuzzController {
  private final FizzBuzzCalculator calculator;

  @Autowired
  public FizzBuzzController(FizzBuzzCalculator calculator) {
    this.calculator = calculator;
  }

  @GetMapping("/number/{input}")
  public String getSingleNumber(@PathVariable int input) {
    return calculator.single(input);
  }

  @GetMapping("/numbers")
  public List<String> getNumberSequence(
      @RequestParam(required = false, defaultValue = "100") int limit) {
    return validateInputs(limit).map(calculator::sequence)
                                .map(this::asJavaList)
                                .getOrElseThrow(identity());
  }

  private Either<ResponseStatusException, Integer> validateInputs(int limit) {
    return Either.<ResponseStatusException, Integer>right(limit).filterOrElse(
        l -> l < 10_000,
        val -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "limit must be less than 10000."));
  }

  private List<String> asJavaList(Stream<String> sequence) {
    return Option.of(sequence)
                 .map(Stream::asJava)
                 .getOrElse(Collections.emptyList());
  }
}
