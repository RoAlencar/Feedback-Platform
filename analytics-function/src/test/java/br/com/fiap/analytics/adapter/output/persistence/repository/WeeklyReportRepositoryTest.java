package br.com.fiap.analytics.adapter.output.persistence.repository;

import br.com.fiap.analytics.adapter.output.persistence.entity.WeeklyReportJpaEntity;
import br.com.fiap.shared.domain.entity.WeeklyReport;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class WeeklyReportRepositoryTest {

    @Inject
    WeeklyReportRepository weeklyReportRepository;

    @Inject
    EntityManager entityManager;

    @Test
    void shouldPersistWeeklyReportSuccessfully() {
        UUID id = UUID.randomUUID();

        WeeklyReport weeklyReport = new WeeklyReport(
                id,
                LocalDate.of(2026, 4, 21),
                LocalDate.of(2026, 4, 27),
                new BigDecimal("4.50"),
                10
        );

        weeklyReportRepository.save(weeklyReport);

        WeeklyReportJpaEntity persisted = entityManager.find(WeeklyReportJpaEntity.class, id);

        assertNotNull(persisted);
        assertEquals(id, persisted.getId());
        assertEquals(LocalDate.of(2026, 4, 21), persisted.getPeriodStart());
        assertEquals(LocalDate.of(2026, 4, 27), persisted.getPeriodEnd());
        assertEquals(new BigDecimal("4.50"), persisted.getAverageScore());
        assertEquals(10, persisted.getTotalFeedbacks());
        assertNotNull(persisted.getGeneratedAt());
    }
}