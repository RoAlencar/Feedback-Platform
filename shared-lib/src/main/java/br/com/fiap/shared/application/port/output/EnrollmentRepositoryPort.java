package br.com.fiap.shared.application.port.output;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.com.fiap.shared.domain.entity.Enrollment;

public interface EnrollmentRepositoryPort {

    void save(Enrollment enrollment);

    Optional<Enrollment> findById(UUID id);

    Optional<Enrollment> findByStudentAndCourse(UUID studentId, UUID courseId);

    List<Enrollment> findByStudentId(UUID studentId);

    List<Enrollment> findByCourseId(UUID courseId);
}
