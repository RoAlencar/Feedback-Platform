package br.com.fiap.shared.application.port.output;

import br.com.fiap.shared.domain.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepositoryPort {

    void save(User user);

    Optional<User> findById(UUID id);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

}
