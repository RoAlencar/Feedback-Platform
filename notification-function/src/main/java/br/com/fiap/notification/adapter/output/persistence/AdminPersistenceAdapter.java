package br.com.fiap.notification.adapter.output.persistence;

import br.com.fiap.notification.adapter.output.persistence.mapper.AdminMapper;
import br.com.fiap.notification.adapter.output.persistence.repository.AdminRepository;
import br.com.fiap.shared.application.port.output.UserRepositoryPort;
import br.com.fiap.shared.domain.entity.Admin;
import br.com.fiap.shared.domain.entity.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class AdminPersistenceAdapter implements UserRepositoryPort {

    private final AdminRepository adminRepository;

    public AdminPersistenceAdapter(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    @Transactional
    public void save(User user) {
        if (!(user instanceof Admin admin)) {
            throw new IllegalArgumentException("Only Admin can be persisted in notification-function");
        }

        adminRepository.persist(AdminMapper.toJpaEntity(admin));
    }

    @Override
    public Optional<User> findById(UUID id) {
        return adminRepository.findByIdOptional(id)
                .map(AdminMapper::toDomain)
                .map(User.class::cast);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return adminRepository.findByEmail(email)
                .map(AdminMapper::toDomain)
                .map(User.class::cast);
    }

    @Override
    public boolean existsByEmail(String email) {
        return adminRepository.existsByEmail(email);
    }
}