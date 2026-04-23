package br.com.fiap.shared.domain.valueObject;

public enum UrgencyLevel {
    LOW,
    MEDIUM,
    HIGH,
    CRITICAL;


    public static UrgencyLevel fromGrade(Grade grade) {

        if(grade.isCritical()) return CRITICAL;
        if (grade.isAttention()) return HIGH;
        if(grade.isWarning()) return MEDIUM;
        if(grade.isElevated()) return LOW;

        throw new IllegalArgumentException("Grade fora das faixas esperadas");
    }
}
