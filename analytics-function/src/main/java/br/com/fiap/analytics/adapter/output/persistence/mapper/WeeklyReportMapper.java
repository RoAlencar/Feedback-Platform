package br.com.fiap.analytics.adapter.output.persistence.mapper;

import br.com.fiap.analytics.adapter.output.persistence.entity.WeeklyReportJpaEntity;

import br.com.fiap.shared.domain.entity.WeeklyReport;

public class WeeklyReportMapper {


    public static WeeklyReportJpaEntity toJpaEntity(WeeklyReport domain){

        WeeklyReportJpaEntity entity = new WeeklyReportJpaEntity();

        entity.setId(domain.getId());
        entity.setPeriodStart(domain.getPeriodStart());
        entity.setPeriodEnd(domain.getPeriodEnd());
        entity.setAverageScore(domain.getAverageScore());
        entity.setTotalFeedbacks(domain.getTotalFeedbacks());
        entity.setGeneratedAt(domain.getGeneratedAt());

        return entity;
    }


    public static WeeklyReport toDomain(WeeklyReportJpaEntity entity){

        return new WeeklyReport(
                entity.getId(),
                entity.getPeriodStart(),
                entity.getPeriodEnd(),
                entity.getAverageScore(),
                entity.getTotalFeedbacks()
        );
    }
}
