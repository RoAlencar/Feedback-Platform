package br.com.fiap.shared.domain.valueObject;

public enum UrgencyLevel {
    LOW,
    MEDIUM,
    HIGH,
    CRITICAL;


    public static UrgencyLevel fromGrade(Score score) {

        if(score.isCritical()) return CRITICAL;
        if (score.isWarning()) return HIGH;
        if(score.isAttention()) return MEDIUM;
        if(score.isElevated()) return LOW;

        throw new IllegalArgumentException("Grade is out of expected ranges");
    }
}
