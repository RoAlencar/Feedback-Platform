package br.com.fiap.shared.domain.valueObject;

public enum Urgency {
    LOW,
    MEDIUM,
    HIGH,
    CRITICAL;

    public static Urgency fromGrade(Score score) {
        int valor = score.valor();
        if (valor <= 2) {
            return CRITICAL;
        } else if (valor <= 5) {
            return HIGH;
        } else if (valor <= 8) {
            return MEDIUM;
        } else {
            return LOW;
        }
    }
}
