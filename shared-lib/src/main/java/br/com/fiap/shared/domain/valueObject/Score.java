package br.com.fiap.shared.domain.valueObject;

import br.com.fiap.shared.domain.exception.ValidationException;

public record Score(int valor) {

    private static final int MIN = 0;
    private static final int MAX = 10;

    private static final int LOW_LIMIT = 10;
    private static final int MEDIUM_LIMIT = 7;
    private static final int HIGH_LIMIT = 4;
    private static final int CRITICAL_LIMIT = 2;


    public Score {
        if (valor < MIN || valor > MAX) {
            throw new ValidationException(
                    "Score must be between " + MIN + " and " + MAX + ", received: " + valor);
        }
    }

    public boolean isCritical() { return valor <= CRITICAL_LIMIT; }

    public boolean isWarning() { return valor <= HIGH_LIMIT; }

    public boolean isAttention()  {  return valor <= MEDIUM_LIMIT; }

    public boolean isElevated()  { return valor <= LOW_LIMIT; }
}