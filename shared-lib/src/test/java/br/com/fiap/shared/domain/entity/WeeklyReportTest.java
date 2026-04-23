package br.com.fiap.shared.domain.entity;

import br.com.fiap.shared.domain.exception.AverageScoreException;
import br.com.fiap.shared.domain.exception.PeriodDateException;
import br.com.fiap.shared.domain.exception.TotalFeedbacksException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class WeeklyReportTest {

    @Test
    void shouldCreateWeeklyReportSuccessfully() {
        UUID id = UUID.randomUUID();
        LocalDate periodStart = LocalDate.of(2026, 4, 20);
        LocalDate periodEnd = LocalDate.of(2026, 4, 26);
        BigDecimal averageScore = new BigDecimal("4.50");
        Integer totalFeedbacks = 10;

        WeeklyReport weeklyReport = new WeeklyReport(
                id,
                periodStart,
                periodEnd,
                averageScore,
                totalFeedbacks
        );

        assertNotNull(weeklyReport);
        assertEquals(id, weeklyReport.getId());
        assertEquals(periodStart, weeklyReport.getPeriodStart());
        assertEquals(periodEnd, weeklyReport.getPeriodEnd());
        assertEquals(averageScore, weeklyReport.getAverageScore());
        assertEquals(totalFeedbacks, weeklyReport.getTotalFeedbacks());
        assertNotNull(weeklyReport.getGeneratedAt());
    }

    @Test
    void shouldThrowExceptionWhenPeriodStartIsAfterPeriodEnd() {
        UUID id = UUID.randomUUID();
        LocalDate periodStart = LocalDate.of(2026, 4, 27);
        LocalDate periodEnd = LocalDate.of(2026, 4, 20);
        BigDecimal averageScore = new BigDecimal("4.50");
        Integer totalFeedbacks = 10;

        assertThrows(PeriodDateException.class, () -> new WeeklyReport(
                id,
                periodStart,
                periodEnd,
                averageScore,
                totalFeedbacks
        ));
    }

    @Test
    void shouldThrowExceptionWhenAverageScoreIsNegative() {
        UUID id = UUID.randomUUID();
        LocalDate periodStart = LocalDate.of(2026, 4, 20);
        LocalDate periodEnd = LocalDate.of(2026, 4, 26);
        BigDecimal averageScore = new BigDecimal("-1.00");
        Integer totalFeedbacks = 10;

        assertThrows(AverageScoreException.class, () -> new WeeklyReport(
                id,
                periodStart,
                periodEnd,
                averageScore,
                totalFeedbacks
        ));
    }


    @Test
    void shouldThrowExceptionWhenTotalFeedbacksIsNegative() {
        UUID id = UUID.randomUUID();
        LocalDate periodStart = LocalDate.of(2026, 4, 20);
        LocalDate periodEnd = LocalDate.of(2026, 4, 26);
        BigDecimal averageScore = new BigDecimal("4.50");
        Integer totalFeedbacks = -1;

        assertThrows(TotalFeedbacksException.class, () -> new WeeklyReport(
                id,
                periodStart,
                periodEnd,
                averageScore,
                totalFeedbacks
        ));
    }

}