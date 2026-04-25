package br.com.fiap.shared.domain.entity;

import br.com.fiap.shared.domain.exception.AverageScoreException;
import br.com.fiap.shared.domain.exception.TotalFeedbacksException;
import br.com.fiap.shared.domain.exception.PeriodDateException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class WeeklyReport {

    private final UUID id;
    private final LocalDate periodStart;
    private final LocalDate periodEnd;
    private final BigDecimal averageScore;
    private final int totalFeedbacks;
    private final LocalDateTime generatedAt;


    public WeeklyReport(
            UUID id,
            LocalDate periodStart,
            LocalDate periodEnd,
            BigDecimal averageScore,
            Integer totalFeedbacks
    ){
        this.id = Objects.requireNonNull(id, "id is required");
        this.periodStart = Objects.requireNonNull(periodStart, "periodStart is required");
        this.periodEnd = Objects.requireNonNull(periodEnd, "periodEnd is required");
        this.averageScore = Objects.requireNonNull(averageScore, "averageScore is required");
        this.totalFeedbacks = Objects.requireNonNull(totalFeedbacks, "totalFeedbacks is required");
        this.generatedAt = LocalDateTime.now();

        validate();
    }


    //GETTERS
    public UUID getId() {
        return id;
    }

    public LocalDate getPeriodStart() {
        return periodStart;
    }

    public LocalDate getPeriodEnd() {
        return periodEnd;
    }

    public BigDecimal getAverageScore() {
        return averageScore;
    }

    public Integer getTotalFeedbacks() {
        return totalFeedbacks;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }


    //VALIDATE
    private void validate(){

        if(periodStart.isAfter(periodEnd)){

            throw new PeriodDateException("periodStart cannot be after periodEnd");
        }

        if(averageScore.compareTo(BigDecimal.ZERO) < 0){

            throw new AverageScoreException("averageScore cannot be negative");
        }

        if (totalFeedbacks < 0) {
            throw new TotalFeedbacksException("totalFeedbacks cannot be negative");
        }
    }
}
