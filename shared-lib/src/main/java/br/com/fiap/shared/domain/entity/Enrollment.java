package br.com.fiap.shared.domain.entity;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

import br.com.fiap.shared.common.DateUtils;
import br.com.fiap.shared.domain.exception.ValidationException;
import br.com.fiap.shared.domain.valueObject.EnrollmentStatus;

public final class Enrollment {

    private final UUID id;
    private final UUID studentId;
    private final UUID courseId;
    private final LocalDate enrollmentDate;
    private final EnrollmentStatus status;

    public Enrollment(UUID id, UUID studentId, UUID courseId, LocalDate enrollmentDate, EnrollmentStatus status) {
        this.id = Objects.requireNonNull(id, "id is required");
        this.studentId = Objects.requireNonNull(studentId, "studentId is required");
        this.courseId = Objects.requireNonNull(courseId, "courseId is required");
        this.enrollmentDate = Objects.requireNonNull(enrollmentDate, "enrollmentDate is required");
        this.status = Objects.requireNonNull(status, "status is required");
        
        validate();
    }

    public static Enrollment create(UUID studentId, UUID courseId) {
        return new Enrollment(
                UUID.randomUUID(),
                studentId,
                courseId,
                DateUtils.now().toLocalDate(),
                EnrollmentStatus.ACTIVE
        );
    }

    private void validate() {
        if (enrollmentDate.isAfter(LocalDate.now())) {
            throw new ValidationException("enrollmentDate cannot be in the future");
        }
    }

    public UUID getId() {
        return id;
    }

    public UUID getStudentId() {
        return studentId;
    }

    public UUID getCourseId() {
        return courseId;
    }

    public LocalDate getEnrollmentDate() {
        return enrollmentDate;
    }

    public EnrollmentStatus getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Enrollment other)) return false;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
