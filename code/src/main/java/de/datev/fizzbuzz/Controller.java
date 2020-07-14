package de.datev.fizzbuzz;

import org.springframework.web.bind.annotation.*;

import java.util.stream.Stream;

@RestController
@RequestMapping("/api/v1/fizz-buzz")
public class Controller {
  private final Calculator calculator;

  public Controller(Calculator calculator) {
    this.calculator = calculator;
  }

  @GetMapping("/numbers/{input}")
  public String calculateSingle(@PathVariable int input) {
    return calculator.singleValue(input);
  }

  @GetMapping("/numbers")
  public Stream<String> calculateSequence(@RequestParam(required = false, defaultValue = "100") int limit) {
    return calculator.sequenceUpTo(limit);
  }
}
