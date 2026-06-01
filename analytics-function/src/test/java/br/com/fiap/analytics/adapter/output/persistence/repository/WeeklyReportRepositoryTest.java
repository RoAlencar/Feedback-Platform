package br.com.fiap.analytics.adapter.output.persistence.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.fiap.analytics.adapter.output.persistence.entity.WeeklyReportJpaEntity;
import br.com.fiap.shared.domain.entity.WeeklyReport;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

@QuarkusTest
@QuarkusTestResource(PostgresTestResource.class)
class WeeklyReportRepositoryTest {

    @Inject
    WeeklyReportRepository weeklyReportRepository;

    @Inject
    EntityManager entityManager;

    @BeforeEach
    @Transactional
    void cleanDatabase() {
        entityManager.createQuery("DELETE FROM DailyReportItemJpaEntity").executeUpdate();
        entityManager.createQuery("DELETE FROM UrgencyReportItemJpaEntity").executeUpdate();
        entityManager.createQuery("DELETE FROM WeeklyReportJpaEntity").executeUpdate();
    }


    @Test
    void shouldPersistWeeklyReportSuccessfully() {
        UUID id = UUID.randomUUID();

        WeeklyReport weeklyReport = new WeeklyReport(
                id,
                LocalDate.of(2026, 4, 21),
                LocalDate.of(2026, 4, 27),
                new BigDecimal("4.50"),
                10);

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