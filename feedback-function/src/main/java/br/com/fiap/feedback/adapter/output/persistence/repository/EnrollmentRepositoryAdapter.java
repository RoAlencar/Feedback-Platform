package br.com.fiap.feedback.adapter.output.persistence.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import br.com.fiap.shared.application.port.output.EnrollmentRepositoryPort;
import br.com.fiap.shared.domain.entity.Enrollment;
import br.com.fiap.feedback.adapter.output.persistence.entity.EnrollmentJpaEntity;
import br.com.fiap.feedback.adapter.output.persistence.mapper.EnrollmentMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class EnrollmentRepositoryAdapter implements EnrollmentRepositoryPort {

    private final EntityManager entityManager;

    public EnrollmentRepositoryAdapter(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public void save(Enrollment enrollment) {
        EnrollmentJpaEntity entity = EnrollmentMapper.toJpaEntity(enrollment);
        entityManager.persist(entity);
    }

    @Override
    public Optional<Enrollment> findById(UUID id) {
        EnrollmentJpaEntity entity = entityManager.find(EnrollmentJpaEntity.class, id);
        return Optional.ofNullable(entity).map(EnrollmentMapper::toDomain);
    }

    @Override
    public Optional<Enrollment> findByStudentAndCourse(UUID studentId, UUID courseId) {
        TypedQuery<EnrollmentJpaEntity> query = entityManager.createQuery(
                "SELECT e FROM EnrollmentJpaEntity e WHERE e.studentId = :studentId AND e.courseId = :courseId",
                EnrollmentJpaEntity.class
        );
        query.setParameter("studentId", studentId);
        query.setParameter("courseId", courseId);
        
        return query.getResultStream()
                .findFirst()
                .map(EnrollmentMapper::toDomain);
    }

    @Override
    public List<Enrollment> findByStudentId(UUID studentId) {
        TypedQuery<EnrollmentJpaEntity> query = entityManager.createQuery(
                "SELECT e FROM EnrollmentJpaEntity e WHERE e.studentId = :studentId",
                EnrollmentJpaEntity.class
        );
        query.setParameter("studentId", studentId);
        
        return query.getResultList()
                .stream()
                .map(EnrollmentMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Enrollment> findByCourseId(UUID courseId) {
        TypedQuery<EnrollmentJpaEntity> query = entityManager.createQuery(
                "SELECT e FROM EnrollmentJpaEntity e WHERE e.courseId = :courseId",
                EnrollmentJpaEntity.class
        );
        query.setParameter("courseId", courseId);
        
        return query.getResultList()
                .stream()
                .map(EnrollmentMapper::toDomain)
                .collect(Collectors.toList());
    }
}
