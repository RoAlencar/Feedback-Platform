package br.com.fiap.analytics.application.port;

import br.com.fiap.shared.domain.entity.WeeklyReport;

public interface WeeklyReportPersistencePort {

    void save(WeeklyReport weeklyReport);
}
