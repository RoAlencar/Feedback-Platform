package br.com.fiap.analytics.adapter.output.persistence.repository;

import br.com.fiap.analytics.adapter.output.persistence.entity.DailyReportItemJpaEntity;
import br.com.fiap.analytics.adapter.output.persistence.entity.UrgencyReportItemJpaEntity;
import br.com.fiap.analytics.adapter.output.persistence.entity.WeeklyReportJpaEntity;
import br.com.fiap.analytics.adapter.output.persistence.mapper.WeeklyReportMapper;
import br.com.fiap.analytics.application.port.WeeklyReportPersistencePort;
import br.com.fiap.shared.domain.entity.WeeklyReport;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class WeeklyReportRepository implements WeeklyReportPersistencePort {

    private final EntityManager entityManager;

    public WeeklyReportRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public void save(WeeklyReport weeklyReport) {
        WeeklyReportJpaEntity entity = WeeklyReportMapper.toJpaEntity(weeklyReport);
        entityManager.persist(entity);
    }

    public List<WeeklyReportJpaEntity> findAll() {
        return entityManager
                .createQuery(
                        "SELECT w FROM WeeklyReportJpaEntity w ORDER BY w.generatedAt DESC",
                        WeeklyReportJpaEntity.class
                )
                .getResultList();
    }

    public List<DailyReportItemJpaEntity> findDailyItemsByWeeklyReportId(UUID weeklyReportId) {
        return entityManager
                .createQuery(
                        """
                        SELECT d
                        FROM DailyReportItemJpaEntity d
                        WHERE d.weeklyReport.id = :weeklyReportId
                        ORDER BY d.date ASC
                        """,
                        DailyReportItemJpaEntity.class
                )
                .setParameter("weeklyReportId", weeklyReportId)
                .getResultList();
    }

    public List<UrgencyReportItemJpaEntity> findUrgencyItemsByWeeklyReportId(UUID weeklyReportId) {
        return entityManager
                .createQuery(
                        """
                        SELECT u
                        FROM UrgencyReportItemJpaEntity u
                        WHERE u.weeklyReport.id = :weeklyReportId
                        ORDER BY u.urgencyLevel ASC
                        """,
                        UrgencyReportItemJpaEntity.class
                )
                .setParameter("weeklyReportId", weeklyReportId)
                .getResultList();
    }
}