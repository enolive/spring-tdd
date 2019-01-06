package de.datev.fizzbuzz;

import io.vavr.collection.Stream;
import io.vavr.control.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/v1/fizz-buzz")
@Validated
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
            @RequestParam(required = false, defaultValue = "100")
            @Max(99999) int limit) {
        return asJavaList(calculator.sequence(limit));
    }

    private List<String> asJavaList(Stream<String> sequence) {
        return Option.of(sequence)
                     .map(Stream::asJava)
                     .getOrElse(Collections.emptyList());
    }
}
