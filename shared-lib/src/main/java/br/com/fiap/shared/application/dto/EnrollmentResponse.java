package br.com.fiap.shared.application.dto;

import java.time.LocalDate;
import java.util.UUID;

import br.com.fiap.shared.domain.entity.Enrollment;
import br.com.fiap.shared.domain.valueObject.EnrollmentStatus;

public record EnrollmentResponse(
        UUID id,
        UUID studentId,
        UUID courseId,
        LocalDate enrollmentDate,
        EnrollmentStatus status) {

    public static EnrollmentResponse fromDomain(Enrollment enrollment) {
        return new EnrollmentResponse(
                enrollment.getId(),
                enrollment.getStudentId(),
                enrollment.getCourseId(),
                enrollment.getEnrollmentDate(),
                enrollment.getStatus()
        );
    }
}
