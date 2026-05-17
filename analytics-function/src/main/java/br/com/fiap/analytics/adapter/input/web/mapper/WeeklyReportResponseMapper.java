package br.com.fiap.analytics.adapter.input.web.mapper;

import br.com.fiap.analytics.adapter.input.web.dto.DailyReportItemResponse;
import br.com.fiap.analytics.adapter.input.web.dto.UrgencyReportItemResponse;
import br.com.fiap.analytics.adapter.input.web.dto.WeeklyReportResponse;
import br.com.fiap.analytics.adapter.output.persistence.entity.DailyReportItemJpaEntity;
import br.com.fiap.analytics.adapter.output.persistence.entity.UrgencyReportItemJpaEntity;
import br.com.fiap.analytics.adapter.output.persistence.entity.WeeklyReportJpaEntity;

import java.util.List;

public final class WeeklyReportResponseMapper {

    private WeeklyReportResponseMapper() {
    }

    public static WeeklyReportResponse toResponse(
            WeeklyReportJpaEntity weeklyReport,
            List<DailyReportItemJpaEntity> dailyItems,
            List<UrgencyReportItemJpaEntity> urgencyItems
    ) {
        return new WeeklyReportResponse(
                weeklyReport.getId(),
                weeklyReport.getPeriodStart(),
                weeklyReport.getPeriodEnd(),
                weeklyReport.getAverageScore(),
                weeklyReport.getTotalFeedbacks(),
                weeklyReport.getGeneratedAt(),
                dailyItems.stream()
                        .map(WeeklyReportResponseMapper::toDailyResponse)
                        .toList(),
                urgencyItems.stream()
                        .map(WeeklyReportResponseMapper::toUrgencyResponse)
                        .toList()
        );
    }

    private static DailyReportItemResponse toDailyResponse(DailyReportItemJpaEntity entity) {
        return new DailyReportItemResponse(
                entity.getDate(),
                entity.getFeedbackCount()
        );
    }

    private static UrgencyReportItemResponse toUrgencyResponse(UrgencyReportItemJpaEntity entity) {
        return new UrgencyReportItemResponse(
                entity.getUrgencyLevel().name(),
                entity.getFeedbackCount()
        );
    }
}