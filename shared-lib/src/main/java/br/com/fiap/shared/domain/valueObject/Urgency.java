package br.com.fiap.shared.domain.valueObject;

public enum Urgency {
    CRITICAL,
    NORMAL;

    public static Urgency fromGrade(Grade grade) {
        return grade.isCritical() ? CRITICAL : NORMAL;
    }
}
