package br.com.fiap.shared.domain.valueObject;

import br.com.fiap.shared.domain.exception.ValidationException;

public record Description(String valor) {

    public Description {
        if (valor == null || valor.isBlank()) {
            throw new ValidationException("Description must not be null or blank");
        }
    }
}