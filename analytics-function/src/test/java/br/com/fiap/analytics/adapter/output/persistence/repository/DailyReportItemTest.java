package br.com.fiap.analytics.adapter.output.persistence.repository;

import br.com.fiap.analytics.adapter.output.persistence.entity.DailyReportItemJpaEntity;
import br.com.fiap.analytics.adapter.output.persistence.entity.WeeklyReportJpaEntity;
import br.com.fiap.analytics.adapter.output.persistence.mapper.WeeklyReportMapper;
import br.com.fiap.shared.domain.entity.DailyReportItem;
import br.com.fiap.shared.domain.entity.WeeklyReport;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class DailyReportItemRepositoryTest {

    @Inject
    DailyReportItemRepository dailyReportItemRepository;

    @Inject
    EntityManager entityManager;

    @BeforeEach
    @Transactional
    void cleanDatabase() {

        entityManager.createQuery("DELETE FROM DailyReportItemJpaEntity")
                .executeUpdate();

        entityManager.createQuery("DELETE FROM WeeklyReportJpaEntity")
                .executeUpdate();
    }

    @Test
    @Transactional
    void shouldPersistDailyReportItemSuccessfully() {

        UUID weeklyReportId = UUID.randomUUID();

        WeeklyReport weeklyReport = new WeeklyReport(
                weeklyReportId,
                LocalDate.of(2026, 4, 21),
                LocalDate.of(2026, 4, 27),
                new BigDecimal("4.50"),
                10
        );

        WeeklyReportJpaEntity weeklyReportJpaEntity =
                WeeklyReportMapper.toJpaEntity(weeklyReport);

        entityManager.persist(weeklyReportJpaEntity);

        DailyReportItem dailyReportItem = new DailyReportItem(
                LocalDate.of(2026, 4, 23),
                3
        );

        dailyReportItemRepository.save(
                dailyReportItem,
                weeklyReportJpaEntity
        );

        List<DailyReportItemJpaEntity> results = entityManager
                .createQuery(
                        "SELECT d FROM DailyReportItemJpaEntity d",
                        DailyReportItemJpaEntity.class
                )
                .getResultList();

        assertEquals(1, results.size());

        DailyReportItemJpaEntity persisted = results.get(0);

        assertNotNull(persisted.getId());
        assertEquals(
                LocalDate.of(2026, 4, 23),
                persisted.getDate()
        );

        assertEquals(3, persisted.getFeedbackCount());

        assertNotNull(persisted.getWeeklyReport());

        assertEquals(
                weeklyReportId,
                persisted.getWeeklyReport().getId()
        );
    }
}