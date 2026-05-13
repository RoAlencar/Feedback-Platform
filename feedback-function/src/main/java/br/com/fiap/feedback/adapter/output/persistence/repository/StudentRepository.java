package br.com.fiap.feedback.adapter.output.persistence.repository;

import br.com.fiap.feedback.adapter.output.persistence.entity.StudentJpaEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class StudentRepository implements PanacheRepositoryBase<StudentJpaEntity, UUID> {

    public Optional<StudentJpaEntity> findByEmail(String email) {
        return find("email", email).firstResultOptional();
    }

    public boolean existsByEmail(String email) {
        return count("email", email) > 0;
    }
}