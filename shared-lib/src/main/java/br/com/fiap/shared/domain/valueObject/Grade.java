package br.com.fiap.shared.domain.valueObject;

import br.com.fiap.shared.domain.exception.ValidationException;

public record Grade(int valor) {

    private static final int MIN = 0;
    private static final int MAX = 10;
    private static final int CRITICAL_LIMIT = 4;

    public Grade {
        if (valor < MIN || valor > MAX) {
            throw new ValidationException(
                    "Nota deve estar entre " + MIN + " e " + MAX + ", recebido: " + valor);
        }
    }

    public boolean isCritical() {
        return valor <= CRITICAL_LIMIT;
    }
}