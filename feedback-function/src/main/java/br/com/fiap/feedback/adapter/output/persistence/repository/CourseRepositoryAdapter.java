package br.com.fiap.feedback.adapter.output.persistence.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import br.com.fiap.shared.application.port.output.CourseRepositoryPort;
import br.com.fiap.shared.domain.entity.Course;
import br.com.fiap.feedback.adapter.output.persistence.entity.CourseJpaEntity;
import br.com.fiap.feedback.adapter.output.persistence.mapper.CourseMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class CourseRepositoryAdapter implements CourseRepositoryPort {

    private final EntityManager entityManager;

    public CourseRepositoryAdapter(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public void save(Course course) {
        CourseJpaEntity entity = CourseMapper.toJpaEntity(course);
        entityManager.persist(entity);
    }

    @Override
    public Optional<Course> findById(UUID id) {
        CourseJpaEntity entity = entityManager.find(CourseJpaEntity.class, id);
        return Optional.ofNullable(entity).map(CourseMapper::toDomain);
    }

    @Override
    public List<Course> findAll() {
        return entityManager.createQuery("SELECT c FROM CourseJpaEntity c", CourseJpaEntity.class)
                .getResultList()
                .stream()
                .map(CourseMapper::toDomain)
                .collect(Collectors.toList());
    }
}
