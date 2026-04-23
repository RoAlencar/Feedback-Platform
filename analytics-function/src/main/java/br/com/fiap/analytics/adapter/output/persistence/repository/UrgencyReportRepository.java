package br.com.fiap.analytics.adapter.output.persistence.repository;

import br.com.fiap.analytics.adapter.output.persistence.entity.UrgencyReportItemJpaEntity;
import br.com.fiap.analytics.adapter.output.persistence.entity.WeeklyReportJpaEntity;
import br.com.fiap.analytics.adapter.output.persistence.mapper.UrgencyReportItemMapper;
import br.com.fiap.analytics.application.port.UrgencyReportItemPersistencePort;
import br.com.fiap.shared.domain.entity.UrgencyReportItem;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class UrgencyReportRepository implements UrgencyReportItemPersistencePort {

    private final EntityManager entityManager;

    public UrgencyReportRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public void save(UrgencyReportItem urgencyReportItem, WeeklyReportJpaEntity weeklyReport) {

        UrgencyReportItemJpaEntity entity = UrgencyReportItemMapper.toEntity(urgencyReportItem, weeklyReport);
        entityManager.persist(entity);
    }
}
