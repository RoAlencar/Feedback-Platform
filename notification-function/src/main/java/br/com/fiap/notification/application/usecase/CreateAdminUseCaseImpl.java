package br.com.fiap.notification.application.usecase;

import br.com.fiap.shared.application.dto.UserRequest;
import br.com.fiap.shared.application.dto.UserResponse;
import br.com.fiap.shared.application.port.input.CreateAdminUseCase;
import br.com.fiap.shared.application.port.output.UserRepositoryPort;
import br.com.fiap.shared.domain.entity.Admin;
import br.com.fiap.shared.domain.exception.UserAlreadyExistsException;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CreateAdminUseCaseImpl implements CreateAdminUseCase {

    private final UserRepositoryPort userRepositoryPort;

    public CreateAdminUseCaseImpl(UserRepositoryPort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }

    @Override
    public UserResponse execute(UserRequest request) {
        if (userRepositoryPort.existsByEmail(request.email())) {
            throw new UserAlreadyExistsException("Admin with this email already exists");
        }

        Admin admin = Admin.create(request.name(), request.email());
        userRepositoryPort.save(admin);

        return UserResponse.fromDomain(admin);
    }
}