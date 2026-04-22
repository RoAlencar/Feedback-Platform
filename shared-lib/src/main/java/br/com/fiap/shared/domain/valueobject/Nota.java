package br.com.fiap.shared.domain.valueobject;

import br.com.fiap.shared.domain.exception.ValidationException;

public record Nota(int valor) {

    private static final int MIN = 0;
    private static final int MAX = 10;
    private static final int LIMITE_CRITICA = 4;

    public Nota {
        if (valor < MIN || valor > MAX) {
            throw new ValidationException(
                    "Nota deve estar entre " + MIN + " e " + MAX + ", recebido: " + valor);
        }
    }

    public boolean isCritica() {
        return valor <= LIMITE_CRITICA;
    }
}
