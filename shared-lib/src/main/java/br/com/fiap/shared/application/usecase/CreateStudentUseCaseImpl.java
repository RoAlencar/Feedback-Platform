package br.com.fiap.shared.application.usecase;

import br.com.fiap.shared.application.dto.UserRequest;
import br.com.fiap.shared.application.dto.UserResponse;
import br.com.fiap.shared.application.port.input.CreateStudentUseCase;
import br.com.fiap.shared.application.port.output.UserRepositoryPort;
import br.com.fiap.shared.domain.entity.Student;
import br.com.fiap.shared.domain.exception.UserAlreadyExistsException;

public class CreateStudentUseCaseImpl implements CreateStudentUseCase {

    private final UserRepositoryPort userRepositoryPort;

    public CreateStudentUseCaseImpl(UserRepositoryPort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }

    @Override
    public UserResponse execute(UserRequest request) {
        Student student = Student.create(request.name(), request.email());

        if (userRepositoryPort.existsByEmail(student.getEmail().value())) {
            throw new UserAlreadyExistsException("user with this email already exists");
        }

        userRepositoryPort.save(student);

        return UserResponse.fromDomain(student);
    }
}
