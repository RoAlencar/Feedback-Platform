package br.com.fiap.shared.application.port.input;

import br.com.fiap.shared.application.dto.UserRequest;
import br.com.fiap.shared.application.dto.UserResponse;

public interface CreateStudentUseCase {
    UserResponse execute(UserRequest request);
}
