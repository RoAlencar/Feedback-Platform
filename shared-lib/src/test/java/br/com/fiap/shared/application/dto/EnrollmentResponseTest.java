package br.com.fiap.shared.application.dto;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

import br.com.fiap.shared.domain.entity.Enrollment;
import br.com.fiap.shared.domain.valueObject.EnrollmentStatus;

public class EnrollmentResponseTest {

    @Test
    void deveConverterDeEnrollmentParaEnrollmentResponse() {
        UUID id = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();
        UUID courseId = UUID.randomUUID();
        LocalDate enrollmentDate = LocalDate.now();
        EnrollmentStatus status = EnrollmentStatus.ACTIVE;

        Enrollment enrollment = new Enrollment(id, studentId, courseId, enrollmentDate, status);

        EnrollmentResponse response = EnrollmentResponse.fromDomain(enrollment);

        assertNotNull(response);
        assertEquals(id, response.id());
        assertEquals(studentId, response.studentId());
        assertEquals(courseId, response.courseId());
        assertEquals(enrollmentDate, response.enrollmentDate());
        assertEquals(status, response.status());
    }

    @Test
    void deveManterTodosOsCamposAposConversao() {
        Enrollment enrollment = Enrollment.create(UUID.randomUUID(), UUID.randomUUID());

        EnrollmentResponse response = EnrollmentResponse.fromDomain(enrollment);

        assertEquals(enrollment.getId(), response.id());
        assertEquals(enrollment.getStudentId(), response.studentId());
        assertEquals(enrollment.getCourseId(), response.courseId());
        assertEquals(enrollment.getEnrollmentDate(), response.enrollmentDate());
        assertEquals(enrollment.getStatus(), response.status());
    }

    @Test
    void deveConverterEnrollmentComStatusInativo() {
        UUID id = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();
        UUID courseId = UUID.randomUUID();
        LocalDate enrollmentDate = LocalDate.now();

        Enrollment enrollment = new Enrollment(id, studentId, courseId, enrollmentDate, EnrollmentStatus.INACTIVE);

        EnrollmentResponse response = EnrollmentResponse.fromDomain(enrollment);

        assertEquals(EnrollmentStatus.INACTIVE, response.status());
    }
}
