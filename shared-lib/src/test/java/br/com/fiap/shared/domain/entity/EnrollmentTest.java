package br.com.fiap.shared.domain.entity;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import br.com.fiap.shared.domain.exception.ValidationException;
import br.com.fiap.shared.domain.valueObject.EnrollmentStatus;

public class EnrollmentTest {

    @Test
    void deveCriarEnrollmentComStatusAtivoPorPadrao() {
        UUID studentId = UUID.randomUUID();
        UUID courseId = UUID.randomUUID();

        Enrollment enrollment = Enrollment.create(studentId, courseId);

        assertEquals(EnrollmentStatus.ACTIVE, enrollment.getStatus());
        assertEquals(studentId, enrollment.getStudentId());
        assertEquals(courseId, enrollment.getCourseId());
    }

    @Test
    void deveGerarIdEDataAutomaticamente() {
        UUID studentId = UUID.randomUUID();
        UUID courseId = UUID.randomUUID();

        Enrollment enrollment = Enrollment.create(studentId, courseId);

        assertNotNull(enrollment.getId());
        assertNotNull(enrollment.getEnrollmentDate());
        assertEquals(LocalDate.now(), enrollment.getEnrollmentDate());
    }

    @Test
    void deveLancarExcecaoQuandoStudentIdForNulo() {
        UUID courseId = UUID.randomUUID();

        assertThrows(NullPointerException.class, () -> 
            Enrollment.create(null, courseId));
    }

    @Test
    void deveLancarExcecaoQuandoCourseIdForNulo() {
        UUID studentId = UUID.randomUUID();

        assertThrows(NullPointerException.class, () -> 
            Enrollment.create(studentId, null));
    }

    @Test
    void deveGerarIdsDistintosParaEnrollmentsDiferentes() {
        UUID studentId = UUID.randomUUID();
        UUID courseId = UUID.randomUUID();

        Enrollment e1 = Enrollment.create(studentId, courseId);
        Enrollment e2 = Enrollment.create(studentId, courseId);

        assertNotNull(e1.getId());
        assertNotNull(e2.getId());
        assertNotEquals(e1.getId(), e2.getId());
    }

    @Test
    void deveLancarExcecaoQuandoDataDeMatriculaForNoFuturo() {
        UUID id = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();
        UUID courseId = UUID.randomUUID();
        LocalDate futureDate = LocalDate.now().plusDays(1);

        assertThrows(ValidationException.class, () -> 
            new Enrollment(id, studentId, courseId, futureDate, EnrollmentStatus.ACTIVE));
    }

    @Test
    void devePermitirDataDeMatriculaNoPassado() {
        UUID id = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();
        UUID courseId = UUID.randomUUID();
        LocalDate pastDate = LocalDate.now().minusDays(10);

        Enrollment enrollment = new Enrollment(id, studentId, courseId, pastDate, EnrollmentStatus.ACTIVE);

        assertEquals(pastDate, enrollment.getEnrollmentDate());
    }

    @Test
    void deveCompararEnrollmentsPorId() {
        UUID id = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();
        UUID courseId = UUID.randomUUID();
        LocalDate date = LocalDate.now();

        Enrollment e1 = new Enrollment(id, studentId, courseId, date, EnrollmentStatus.ACTIVE);
        Enrollment e2 = new Enrollment(id, UUID.randomUUID(), UUID.randomUUID(), date, EnrollmentStatus.INACTIVE);

        assertEquals(e1, e2);
        assertEquals(e1.hashCode(), e2.hashCode());
    }
}
