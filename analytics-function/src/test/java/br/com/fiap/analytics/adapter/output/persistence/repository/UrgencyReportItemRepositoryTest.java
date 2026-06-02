package br.com.fiap.analytics.adapter.output.persistence.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

import br.com.fiap.analytics.adapter.output.persistence.entity.UrgencyReportItemJpaEntity;
import br.com.fiap.analytics.adapter.output.persistence.entity.WeeklyReportJpaEntity;
import br.com.fiap.analytics.adapter.output.persistence.mapper.WeeklyReportMapper;
import br.com.fiap.shared.domain.entity.UrgencyReportItem;
import br.com.fiap.shared.domain.entity.WeeklyReport;
import br.com.fiap.shared.domain.valueObject.UrgencyLevel;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@QuarkusTest
@QuarkusTestResource(PostgresTestResource.class)
class UrgencyReportItemRepositoryTest {

        @Inject
        UrgencyReportItemRepository urgencyReportItemRepository;

        @Inject
        EntityManager entityManager;

        @Test
        @Transactional
        void shouldPersistUrgencyReportItemSuccessfully() {

                entityManager.createQuery("DELETE FROM UrgencyReportItemJpaEntity").executeUpdate();
                entityManager.createQuery("DELETE FROM WeeklyReportJpaEntity").executeUpdate();

                UUID weeklyReportId = UUID.randomUUID();

                WeeklyReport weeklyReport = new WeeklyReport(
                                weeklyReportId,
                                LocalDate.of(2026, 4, 21),
                                LocalDate.of(2026, 4, 27),
                                new BigDecimal("4.50"),
                                10);

                WeeklyReportJpaEntity weeklyReportJpaEntity = WeeklyReportMapper.toJpaEntity(weeklyReport);

                entityManager.persist(weeklyReportJpaEntity);

                UrgencyReportItem urgencyReportItem = new UrgencyReportItem(
                                UrgencyLevel.CRITICAL,
                                4);

                urgencyReportItemRepository.save(
                                urgencyReportItem,
                                weeklyReportJpaEntity);

                List<UrgencyReportItemJpaEntity> results = entityManager
                                .createQuery(
                                                "SELECT u FROM UrgencyReportItemJpaEntity u",
                                                UrgencyReportItemJpaEntity.class)
                                .getResultList();

                assertEquals(1, results.size());

                UrgencyReportItemJpaEntity persisted = results.get(0);

                assertNotNull(persisted.getId());
                assertEquals(UrgencyLevel.CRITICAL, persisted.getUrgencyLevel());
                assertEquals(4, persisted.getFeedbackCount());
                assertNotNull(persisted.getWeeklyReport());
                assertEquals(weeklyReportId, persisted.getWeeklyReport().getId());
        }
}