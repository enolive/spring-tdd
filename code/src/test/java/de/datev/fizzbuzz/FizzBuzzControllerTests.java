package de.datev.fizzbuzz;

import io.vavr.collection.Stream;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {FizzBuzzController.class})
class FizzBuzzControllerTests {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private FizzBuzzCalculator calculator;

    @Test
    void getSingleNumber() throws Exception {
        when(calculator.single(anyInt())).thenReturn("result!");

        mvc.perform(get("/api/v1/fizz-buzz/number/42"))
           .andExpect(status().isOk())
           .andExpect(content().string("result!"));
        verify(calculator, times(1)).single(42);
    }

    @Test
    void getNumberSequence() throws Exception {
        when(calculator.sequence(anyInt())).thenReturn(Stream.of("first", "second"));

        mvc.perform(get("/api/v1/fizz-buzz/numbers").param("limit", "42"))
           .andExpect(status().isOk())
           .andExpect(content().json("[\"first\", \"second\"]"));
        verify(calculator, times(1)).sequence(42);
    }

    @Test
    void getNumberSequenceWithDefaultLimit() throws Exception {
        mvc.perform(get("/api/v1/fizz-buzz/numbers"))
           .andExpect(status().isOk());
        verify(calculator, times(1)).sequence(100);
    }

    @Test
    void getNumberSequenceWithTooLargeLimit() throws Exception {
        mvc.perform(get("/api/v1/fizz-buzz/numbers").param("limit", "100000"))
           .andExpect(status().isBadRequest())
           .andExpect(status().reason("limit must be less than 10000."));
        verify(calculator, never()).sequence(anyInt());
    }
}
