package br.com.fiap.shared.domain.entity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import br.com.fiap.shared.domain.exception.ValidationException;
import br.com.fiap.shared.domain.valueObject.CourseName;

public class CourseTest {

    @Test
    void shouldCreateCourseWithValidName() {
        Course course = Course.create("Java Programming");

        assertEquals("Java Programming", course.getName().value());
        assertNotNull(course.getId());
    }

    @Test
    void shouldGenerateUniqueIdsForDifferentCourses() {
        Course course1 = Course.create("Java Programming");
        Course course2 = Course.create("Python Basics");

        assertNotEquals(course1.getId(), course2.getId());
    }

    @Test
    void shouldThrowExceptionWhenNameIsBlank() {
        ValidationException exception = assertThrows(ValidationException.class, () -> Course.create(""));
        assertEquals("Course name cannot be null or blank", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenNameIsWhitespace() {
        ValidationException exception = assertThrows(ValidationException.class, () -> Course.create("   "));
        assertEquals("Course name cannot be null or blank", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenNameIsNull() {
        ValidationException exception = assertThrows(ValidationException.class, () -> Course.create(null));
        assertEquals("Course name cannot be null or blank", exception.getMessage());
    }

    @Test
    void shouldConsiderCoursesEqualByIdOnly() {
        UUID id = UUID.randomUUID();
        Course course1 = new Course(id, new CourseName("Java"));
        Course course2 = new Course(id, new CourseName("Python"));

        assertEquals(course1, course2);
    }

    @Test
    void shouldConsiderCoursesNotEqualWithDifferentIds() {
        Course course1 = Course.create("Java Programming");
        Course course2 = Course.create("Java Programming");

        assertNotEquals(course1, course2);
    }
}
