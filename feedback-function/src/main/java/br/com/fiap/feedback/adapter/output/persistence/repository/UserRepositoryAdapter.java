package br.com.fiap.feedback.adapter.output.persistence.repository;

import br.com.fiap.feedback.adapter.output.persistence.entity.StudentEntity;
import br.com.fiap.shared.application.port.output.UserRepositoryPort;
import br.com.fiap.shared.domain.entity.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final EntityManager entityManager;

    public UserRepositoryAdapter(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public void save(User user) {
        entityManager.persist(StudentEntity.fromDomain((br.com.fiap.shared.domain.entity.Student) user));
    }

    @Override
    public Optional<User> findById(UUID id) {
        StudentEntity entity = entityManager.find(StudentEntity.class, id);
        return Optional.ofNullable(entity).map(StudentEntity::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return entityManager.createQuery(
                        "SELECT s FROM StudentEntity s WHERE s.email = :email", StudentEntity.class)
                .setParameter("email", email)
                .getResultStream()
                .findFirst()
                .map(StudentEntity::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return findByEmail(email).isPresent();
    }
}
