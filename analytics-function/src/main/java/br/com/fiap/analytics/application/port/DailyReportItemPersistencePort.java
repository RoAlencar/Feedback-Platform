package br.com.fiap.analytics.application.port;

import br.com.fiap.analytics.adapter.output.persistence.entity.WeeklyReportJpaEntity;
import br.com.fiap.shared.domain.entity.DailyReportItem;

public interface DailyReportItemPersistencePort {

    void save(DailyReportItem dailyReportItem, WeeklyReportJpaEntity weeklyReport);
}
