package br.com.fiap.shared.domain.valueObject;

import br.com.fiap.shared.domain.exception.ValidationException;

public record CourseName(String value) {

    public CourseName {
        if (value == null || value.isBlank()) {
            throw new ValidationException("Course name cannot be null or blank");
        }

        if (value.length() > 255) {
            throw new ValidationException("Course name cannot exceed 255 characters");
        }
    }
}
