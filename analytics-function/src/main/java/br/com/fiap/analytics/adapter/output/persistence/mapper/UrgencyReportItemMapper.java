package br.com.fiap.analytics.adapter.output.persistence.mapper;

import br.com.fiap.analytics.adapter.output.persistence.entity.UrgencyReportItemJpaEntity;
import br.com.fiap.analytics.adapter.output.persistence.entity.WeeklyReportJpaEntity;
import br.com.fiap.shared.domain.entity.UrgencyReportItem;

import java.util.UUID;

public class UrgencyReportItemMapper {


    public static UrgencyReportItemJpaEntity toEntity(
            UrgencyReportItem domain,
            WeeklyReportJpaEntity weeklyReport
    ){

        UrgencyReportItemJpaEntity entity = new UrgencyReportItemJpaEntity();

        entity.setId(UUID.randomUUID());
        entity.setWeeklyReport(weeklyReport);
        entity.setUrgencyLevel(domain.getUrgency());
        entity.setFeedbackCount(domain.getFeedbackCount());

        return entity;
    }


    public static UrgencyReportItem toDomain(UrgencyReportItemJpaEntity entity){

        return new UrgencyReportItem(
                entity.getUrgencyLevel(),
                entity.getFeedbackCount()
        );
    }
}
