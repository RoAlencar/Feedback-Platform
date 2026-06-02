package br.com.fiap.feedback.application.usecase;

import br.com.fiap.shared.application.dto.UserRequest;
import br.com.fiap.shared.application.dto.UserResponse;
import br.com.fiap.shared.application.port.input.CreateStudentUseCase;
import br.com.fiap.shared.application.port.output.UserRepositoryPort;
import br.com.fiap.shared.domain.entity.Student;
import br.com.fiap.shared.domain.exception.UserAlreadyExistsException;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CreateStudentUseCaseImpl implements CreateStudentUseCase {

    private final UserRepositoryPort userRepositoryPort;

    public CreateStudentUseCaseImpl(UserRepositoryPort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }

    @Override
    public UserResponse execute(UserRequest request) {
        if (userRepositoryPort.existsByEmail(request.email())) {
            throw new UserAlreadyExistsException("Student with this email already exists");
        }

        Student student = Student.create(request.name(), request.email());
        userRepositoryPort.save(student);

        return UserResponse.fromDomain(student);
    }
}