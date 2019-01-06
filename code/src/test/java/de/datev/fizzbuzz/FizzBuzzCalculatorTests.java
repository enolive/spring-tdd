package de.datev.fizzbuzz;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class FizzBuzzCalculatorTests {
    @Autowired
    private FizzBuzzCalculator calculator;

    @ParameterizedTest
    @CsvSource(value = {
            "1, 1, replication",
            "2, 2, replication",
            "3, Fizz, divisible by 3",
            "6, Fizz, divisible by 3",
            "5, Buzz, divisible by 5",
            "10, Buzz, divisible by 5",
            "15, Fizz-Buzz, divisible by 3 and 5",
            "30, Fizz-Buzz, divisible by 3 and 5",
    })
    void single(int input, String expected, @SuppressWarnings("unused") String description) {
        assertThat(calculator.single(input)).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "0, empty list, ''",
            "1, single element, '1'",
            "3, multiple elements, '1, 2, Fizz'",
            "15, all variations, '1, 2, Fizz, 4, Buzz, Fizz, 7, 8, Fizz, Buzz, 11, Fizz, 13, 14, Fizz-Buzz'",
    })
    void sequence(int limit,
                  @SuppressWarnings("unused") String description,
                  @ConvertWith(CsvToStringArray.class) String... values) {
        assertThat(calculator.sequence(limit)).containsExactly(values);
    }

}

