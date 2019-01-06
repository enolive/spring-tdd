package de.datev.fizzbuzz;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.io.IOException;

@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(ConstraintViolationException.class)
    public void handleBeanValidationErrors(ConstraintViolationException exception, HttpServletResponse response)
            throws IOException {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, exception.getLocalizedMessage());
    }
}
