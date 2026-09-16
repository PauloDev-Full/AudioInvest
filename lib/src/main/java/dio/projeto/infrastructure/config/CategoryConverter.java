package dio.projeto.infrastructure.config;

import dio.projeto.domain.Category;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Locale;

@Component
public class CategoryConverter implements Converter<String, Category> {

    @Override
    public Category convert(String source) {
        var normalized = source.trim().toUpperCase(Locale.ROOT).replace('-', '_');
        return Arrays.stream(Category.values())
                .filter(c -> c.name().equals(normalized))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "categoria invalida: " + source + ". Use " + Arrays.toString(Category.values())));
    }
}