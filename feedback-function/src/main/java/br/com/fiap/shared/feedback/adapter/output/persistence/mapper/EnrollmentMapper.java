package br.com.fiap.shared.feedback.adapter.output.persistence.mapper;

import br.com.fiap.shared.domain.entity.Enrollment;
import br.com.fiap.shared.feedback.adapter.output.persistence.entity.EnrollmentJpaEntity;

public class EnrollmentMapper {

    public static EnrollmentJpaEntity toJpaEntity(Enrollment domain) {
        EnrollmentJpaEntity entity = new EnrollmentJpaEntity();
        entity.setId(domain.getId());
        entity.setStudentId(domain.getStudentId());
        entity.setCourseId(domain.getCourseId());
        entity.setEnrollmentDate(domain.getEnrollmentDate());
        entity.setStatus(domain.getStatus());
        return entity;
    }

    public static Enrollment toDomain(EnrollmentJpaEntity entity) {
        return new Enrollment(
                entity.getId(),
                entity.getStudentId(),
                entity.getCourseId(),
                entity.getEnrollmentDate(),
                entity.getStatus()
        );
    }
}
