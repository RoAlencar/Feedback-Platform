package br.com.fiap.analytics.application.usecase;

import br.com.fiap.analytics.adapter.input.web.dto.WeeklyReportResponse;
import br.com.fiap.analytics.adapter.input.web.mapper.WeeklyReportResponseMapper;
import br.com.fiap.analytics.adapter.output.persistence.entity.DailyReportItemJpaEntity;
import br.com.fiap.analytics.adapter.output.persistence.entity.UrgencyReportItemJpaEntity;
import br.com.fiap.analytics.adapter.output.persistence.repository.WeeklyReportRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class GetWeeklyReportsUseCase {

    private final WeeklyReportRepository weeklyReportRepository;

    public GetWeeklyReportsUseCase(WeeklyReportRepository weeklyReportRepository) {
        this.weeklyReportRepository = weeklyReportRepository;
    }

    public List<WeeklyReportResponse> execute() {
        return weeklyReportRepository.findAll()
                .stream()
                .map(weeklyReport -> {
                    List<DailyReportItemJpaEntity> dailyItems =
                            weeklyReportRepository.findDailyItemsByWeeklyReportId(weeklyReport.getId());

                    List<UrgencyReportItemJpaEntity> urgencyItems =
                            weeklyReportRepository.findUrgencyItemsByWeeklyReportId(weeklyReport.getId());

                    return WeeklyReportResponseMapper.toResponse(
                            weeklyReport,
                            dailyItems,
                            urgencyItems
                    );
                })
                .toList();
    }
}