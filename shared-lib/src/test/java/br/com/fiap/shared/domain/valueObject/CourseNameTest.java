package br.com.fiap.shared.domain.valueObject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import br.com.fiap.shared.domain.exception.ValidationException;

public class CourseNameTest {

    @Test
    void shouldCreateCourseNameWithValidValue() {
        CourseName name = new CourseName("Java Programming");

        assertEquals("Java Programming", name.value());
    }

    @Test
    void shouldThrowExceptionWhenValueIsNull() {
        ValidationException exception = assertThrows(ValidationException.class, () -> new CourseName(null));
        assertEquals("Nome do curso nao pode ser nulo ou vazio", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenValueIsBlank() {
        ValidationException exception = assertThrows(ValidationException.class, () -> new CourseName("   "));
        assertEquals("Nome do curso nao pode ser nulo ou vazio", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenValueIsEmpty() {
        ValidationException exception = assertThrows(ValidationException.class, () -> new CourseName(""));
        assertEquals("Nome do curso nao pode ser nulo ou vazio", exception.getMessage());
    }
}
