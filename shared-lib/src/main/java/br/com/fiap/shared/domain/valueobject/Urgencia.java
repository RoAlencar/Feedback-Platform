package br.com.fiap.shared.domain.valueobject;

public enum Urgencia {
    CRITICA,
    NORMAL;

    public static Urgencia fromNota(Nota nota) {
        return nota.isCritica() ? CRITICA : NORMAL;
    }
}
