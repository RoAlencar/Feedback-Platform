package br.com.fiap.notification.adapter.output.persistence.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.com.fiap.notification.adapter.output.persistence.entity.AdminJpaEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AdminRepository implements PanacheRepositoryBase<AdminJpaEntity, UUID> {

    public Optional<AdminJpaEntity> findByEmail(String email) {
        return find("email", email).firstResultOptional();
    }

    public boolean existsByEmail(String email) {
        return count("email", email) > 0;
    }

    public List<AdminJpaEntity> findAllActive() {
        return list("active", true);
    }
}