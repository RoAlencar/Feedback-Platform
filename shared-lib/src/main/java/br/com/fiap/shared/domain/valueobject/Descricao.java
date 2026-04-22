package br.com.fiap.shared.domain.valueobject;

import br.com.fiap.shared.domain.exception.ValidationException;

public record Descricao(String valor) {

    public Descricao {
        if (valor == null || valor.isBlank()) {
            throw new ValidationException("Descricao nao pode ser nula ou vazia");
        }
    }
}
