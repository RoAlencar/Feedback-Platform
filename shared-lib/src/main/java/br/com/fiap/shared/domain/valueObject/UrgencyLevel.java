package br.com.fiap.shared.domain.valueObject;

public enum UrgencyLevel {
    LOW,
    MEDIUM,
    HIGH,
    CRITICAL;


    public static UrgencyLevel fromGrade(Score score) {

        if(score.isCritical()) return CRITICAL;
        if (score.isAttention()) return HIGH;
        if(score.isWarning()) return MEDIUM;
        if(score.isElevated()) return LOW;

        throw new IllegalArgumentException("Grade fora das faixas esperadas");
    }
}
