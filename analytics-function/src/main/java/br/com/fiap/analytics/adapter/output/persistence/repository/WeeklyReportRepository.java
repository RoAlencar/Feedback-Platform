package br.com.fiap.analytics.adapter.output.persistence.repository;

import br.com.fiap.analytics.adapter.output.persistence.entity.WeeklyReportJpaEntity;
import br.com.fiap.analytics.adapter.output.persistence.mapper.WeeklyReportMapper;
import br.com.fiap.analytics.application.port.WeeklyReportPersistencePort;
import br.com.fiap.shared.domain.entity.WeeklyReport;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

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
}
