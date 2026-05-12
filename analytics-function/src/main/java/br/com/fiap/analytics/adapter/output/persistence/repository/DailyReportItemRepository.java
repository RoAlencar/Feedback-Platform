package br.com.fiap.analytics.adapter.output.persistence.repository;

import br.com.fiap.analytics.adapter.output.persistence.entity.DailyReportItemJpaEntity;
import br.com.fiap.analytics.adapter.output.persistence.entity.WeeklyReportJpaEntity;
import br.com.fiap.analytics.adapter.output.persistence.mapper.DailyReportItemMapper;
import br.com.fiap.analytics.application.port.DailyReportItemPersistencePort;
import br.com.fiap.shared.domain.entity.DailyReportItem;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class DailyReportItemRepository implements DailyReportItemPersistencePort {

    private final EntityManager entityManager;

    public DailyReportItemRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public void save(DailyReportItem dailyReportItem, WeeklyReportJpaEntity weeklyReport) {

        DailyReportItemJpaEntity entity = DailyReportItemMapper.toJpaEntity(dailyReportItem, weeklyReport);
        entityManager.persist(entity);
    }
}
