package br.com.fiap.analytics.adapter.output.persistence.mapper;

import br.com.fiap.analytics.adapter.output.persistence.entity.DailyReportItemJpaEntity;
import br.com.fiap.analytics.adapter.output.persistence.entity.WeeklyReportJpaEntity;
import br.com.fiap.shared.domain.entity.DailyReportItem;

import java.util.UUID;

public class DailyReportItemMapper {

    public static DailyReportItemJpaEntity toJpaEntity(
            DailyReportItem domain,
            WeeklyReportJpaEntity weeklyReport
    ){

        DailyReportItemJpaEntity entity = new DailyReportItemJpaEntity();

        entity.setId(UUID.randomUUID());
        entity.setWeeklyReport(weeklyReport);
        entity.setDate(domain.getDate());
        entity.setFeedbackCount(domain.getFeedbackCount());

        return entity;
    }


    public static DailyReportItem toDomain(DailyReportItemJpaEntity entity){

        return new DailyReportItem(
                entity.getDate(),
                entity.getFeedbackCount()
        );
    }
}
