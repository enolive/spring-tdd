package de.datev.fizzbuzz;

import io.vavr.control.Option;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;

class CsvToStringArray extends SimpleArgumentConverter {
    @Override
    protected Object convert(Object source, Class<?> targetType) throws ArgumentConversionException {
        return Option.of(source)
                     .filter(s -> s instanceof String)
                     .map(s -> (String) s)
                     .filter(s -> !s.isEmpty())
                     .map(s -> s.split("\\s*,\\s"))
                     .getOrElse(() -> new String[0]);
    }
}
