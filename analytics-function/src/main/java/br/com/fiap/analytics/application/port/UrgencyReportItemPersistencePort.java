package br.com.fiap.analytics.application.port;

import br.com.fiap.analytics.adapter.output.persistence.entity.WeeklyReportJpaEntity;
import br.com.fiap.shared.domain.entity.UrgencyReportItem;

public interface UrgencyReportItemPersistencePort {

    void save(UrgencyReportItem urgencyReportItem, WeeklyReportJpaEntity weeklyReport);
}
