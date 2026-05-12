package br.com.fiap.shared.application.dto;

import br.com.fiap.shared.domain.entity.User;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String name,
        String email,
        boolean active,
        String type) {

    public static UserResponse fromDomain(User user) {
        return new UserResponse(
                user.getId(),
                user.getName().value(),
                user.getEmail().value(),
                user.isActive(),
                user.getType()
        );
    }
}
