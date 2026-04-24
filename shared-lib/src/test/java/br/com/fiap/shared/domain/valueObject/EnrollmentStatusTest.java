package br.com.fiap.shared.domain.valueObject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class EnrollmentStatusTest {

    @Test
    void shouldContainExactlyTwoValues() {
        assertEquals(2, EnrollmentStatus.values().length);
    }

    @Test
    void shouldContainActiveStatus() {
        assertEquals("ACTIVE", EnrollmentStatus.ACTIVE.name());
    }

    @Test
    void shouldContainInactiveStatus() {
        assertEquals("INACTIVE", EnrollmentStatus.INACTIVE.name());
    }

    @Test
    void shouldResolveActiveByName() {
        assertEquals(EnrollmentStatus.ACTIVE, EnrollmentStatus.valueOf("ACTIVE"));
    }

    @Test
    void shouldResolveInactiveByName() {
        assertEquals(EnrollmentStatus.INACTIVE, EnrollmentStatus.valueOf("INACTIVE"));
    }
}
