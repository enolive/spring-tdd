package de.datev.fizzbuzz;

import io.vavr.collection.Stream;
import io.vavr.control.Either;
import io.vavr.control.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/fizz-buzz")
public class FizzBuzzController {
  private final FizzBuzzCalculator calculator;

  @Autowired
  public FizzBuzzController(FizzBuzzCalculator calculator) {
    this.calculator = calculator;
  }

  @GetMapping("/number/{input}")
  public Mono<String> getSingleNumber(@PathVariable int input) {
    return Mono.just(input)
               .map(calculator::single);
  }

  @GetMapping("/numbers")
  public Mono<List<String>> getNumberSequence(
      @RequestParam(required = false, defaultValue = "100") int limit) {
    return validateInputs(limit).map(calculator::sequence)
                                .fold(Mono::error, this::wrapResultListInMono);
  }

  private Either<ResponseStatusException, Integer> validateInputs(int limit) {
    return Either
        .<ResponseStatusException, Integer>right(limit)
        .filterOrElse(
            l -> l < 10_000,
            val -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "limit must be less than 10000."));
  }

  private Mono<List<String>> wrapResultListInMono(Stream<String> sequence) {
    return Option.of(sequence)
                 .map(Flux::fromIterable)
                 .map(Flux::collectList)
                 .getOrElse(Mono.empty());
  }
}
