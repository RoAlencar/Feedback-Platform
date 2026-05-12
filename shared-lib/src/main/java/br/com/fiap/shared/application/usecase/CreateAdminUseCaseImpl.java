package br.com.fiap.shared.application.usecase;

import br.com.fiap.shared.application.dto.UserRequest;
import br.com.fiap.shared.application.dto.UserResponse;
import br.com.fiap.shared.application.port.input.CreateAdminUseCase;
import br.com.fiap.shared.application.port.output.UserRepositoryPort;
import br.com.fiap.shared.domain.entity.Admin;
import br.com.fiap.shared.domain.exception.UserAlreadyExistsException;

public class CreateAdminUseCaseImpl implements CreateAdminUseCase {

    private final UserRepositoryPort userRepositoryPort;

    public CreateAdminUseCaseImpl(UserRepositoryPort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }

    @Override
    public UserResponse execute(UserRequest request) {
        Admin admin = Admin.create(request.name(), request.email());

        if (userRepositoryPort.existsByEmail(admin.getEmail().value())) {
            throw new UserAlreadyExistsException("user with this email already exists");
        }

        userRepositoryPort.save(admin);

        return UserResponse.fromDomain(admin);
    }
}
