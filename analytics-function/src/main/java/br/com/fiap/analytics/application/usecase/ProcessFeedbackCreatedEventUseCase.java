package br.com.fiap.analytics.application.usecase;

import br.com.fiap.analytics.adapter.output.persistence.entity.AnalyticsFeedbackEventJpaEntity;
import br.com.fiap.analytics.adapter.output.persistence.entity.DailyReportItemJpaEntity;
import br.com.fiap.analytics.adapter.output.persistence.entity.UrgencyReportItemJpaEntity;
import br.com.fiap.analytics.adapter.output.persistence.entity.WeeklyReportJpaEntity;
import br.com.fiap.shared.application.dto.FeedbackEventDTO;
import br.com.fiap.shared.domain.valueObject.UrgencyLevel;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class ProcessFeedbackCreatedEventUseCase {

    private static final Logger LOG = Logger.getLogger(ProcessFeedbackCreatedEventUseCase.class);

    private final EntityManager entityManager;

    public ProcessFeedbackCreatedEventUseCase(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public void execute(FeedbackEventDTO dto) {
        if (dto == null || dto.feedbackId() == null) {
            LOG.warn("Evento de feedback inválido recebido no Analytics.");
            return;
        }

        AnalyticsFeedbackEventJpaEntity existingEvent =
                entityManager.find(AnalyticsFeedbackEventJpaEntity.class, dto.feedbackId());

        if (existingEvent != null) {
            LOG.infof("Feedback %s já processado pelo Analytics. Evento ignorado.", dto.feedbackId());
            return;
        }

        LocalDateTime submittedAt = dto.createdAt() != null
                ? dto.createdAt()
                : LocalDateTime.now();

        Integer score = dto.grade();

        UrgencyLevel urgency = UrgencyLevel.valueOf(dto.urgency());

        AnalyticsFeedbackEventJpaEntity eventEntity = new AnalyticsFeedbackEventJpaEntity();
        eventEntity.setFeedbackId(dto.feedbackId());
        eventEntity.setDescription(dto.description());
        eventEntity.setScore(score);
        eventEntity.setUrgencyLevel(urgency);
        eventEntity.setSubmittedAt(submittedAt);
        eventEntity.setProcessedAt(LocalDateTime.now());

        entityManager.persist(eventEntity);

        rebuildWeeklyReport(submittedAt.toLocalDate());

        LOG.infof(
                "Analytics atualizado para feedbackId=%s score=%s urgência=%s",
                dto.feedbackId(),
                score,
                urgency
        );
    }

    private void rebuildWeeklyReport(LocalDate referenceDate) {
        LocalDate periodStart = referenceDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate periodEnd = periodStart.plusDays(6);

        LocalDateTime startDateTime = periodStart.atStartOfDay();
        LocalDateTime endExclusive = periodEnd.plusDays(1).atStartOfDay();

        List<AnalyticsFeedbackEventJpaEntity> events = entityManager
                .createQuery(
                        """
                        SELECT e
                        FROM AnalyticsFeedbackEventJpaEntity e
                        WHERE e.submittedAt >= :startDateTime
                          AND e.submittedAt < :endExclusive
                        """,
                        AnalyticsFeedbackEventJpaEntity.class
                )
                .setParameter("startDateTime", startDateTime)
                .setParameter("endExclusive", endExclusive)
                .getResultList();

        if (events.isEmpty()) {
            return;
        }

        WeeklyReportJpaEntity weeklyReport = findOrCreateWeeklyReport(periodStart, periodEnd);

        removeExistingItems(weeklyReport.getId());

        int totalFeedbacks = events.size();

        BigDecimal averageScore = BigDecimal.valueOf(
                        events.stream()
                                .mapToInt(AnalyticsFeedbackEventJpaEntity::getScore)
                                .average()
                                .orElse(0.0)
                )
                .setScale(2, RoundingMode.HALF_UP);

        weeklyReport.setTotalFeedbacks(totalFeedbacks);
        weeklyReport.setAverageScore(averageScore);
        weeklyReport.setGeneratedAt(LocalDateTime.now());

        createDailyItems(weeklyReport, events);
        createUrgencyItems(weeklyReport, events);
    }

    private WeeklyReportJpaEntity findOrCreateWeeklyReport(LocalDate periodStart, LocalDate periodEnd) {
        List<WeeklyReportJpaEntity> reports = entityManager
                .createQuery(
                        """
                        SELECT w
                        FROM WeeklyReportJpaEntity w
                        WHERE w.periodStart = :periodStart
                          AND w.periodEnd = :periodEnd
                        """,
                        WeeklyReportJpaEntity.class
                )
                .setParameter("periodStart", periodStart)
                .setParameter("periodEnd", periodEnd)
                .getResultList();

        if (!reports.isEmpty()) {
            return reports.get(0);
        }

        WeeklyReportJpaEntity weeklyReport = new WeeklyReportJpaEntity();
        weeklyReport.setId(UUID.randomUUID());
        weeklyReport.setPeriodStart(periodStart);
        weeklyReport.setPeriodEnd(periodEnd);
        weeklyReport.setAverageScore(BigDecimal.ZERO);
        weeklyReport.setTotalFeedbacks(0);
        weeklyReport.setGeneratedAt(LocalDateTime.now());

        entityManager.persist(weeklyReport);

        return weeklyReport;
    }

    private void removeExistingItems(UUID weeklyReportId) {
        entityManager
                .createQuery(
                        """
                        DELETE FROM DailyReportItemJpaEntity d
                        WHERE d.weeklyReport.id = :weeklyReportId
                        """
                )
                .setParameter("weeklyReportId", weeklyReportId)
                .executeUpdate();

        entityManager
                .createQuery(
                        """
                        DELETE FROM UrgencyReportItemJpaEntity u
                        WHERE u.weeklyReport.id = :weeklyReportId
                        """
                )
                .setParameter("weeklyReportId", weeklyReportId)
                .executeUpdate();
    }

    private void createDailyItems(
            WeeklyReportJpaEntity weeklyReport,
            List<AnalyticsFeedbackEventJpaEntity> events
    ) {
        Map<LocalDate, Long> feedbacksByDate = events.stream()
                .collect(Collectors.groupingBy(
                        event -> event.getSubmittedAt().toLocalDate(),
                        Collectors.counting()
                ));

        feedbacksByDate.forEach((date, count) -> {
            DailyReportItemJpaEntity item = new DailyReportItemJpaEntity();
            item.setId(UUID.randomUUID());
            item.setWeeklyReport(weeklyReport);
            item.setDate(date);
            item.setFeedbackCount(count.intValue());

            entityManager.persist(item);
        });
    }

    private void createUrgencyItems(
            WeeklyReportJpaEntity weeklyReport,
            List<AnalyticsFeedbackEventJpaEntity> events
    ) {
        Map<UrgencyLevel, Long> feedbacksByUrgency = events.stream()
                .collect(Collectors.groupingBy(
                        AnalyticsFeedbackEventJpaEntity::getUrgencyLevel,
                        Collectors.counting()
                ));

        feedbacksByUrgency.forEach((urgency, count) -> {
            UrgencyReportItemJpaEntity item = new UrgencyReportItemJpaEntity();
            item.setId(UUID.randomUUID());
            item.setWeeklyReport(weeklyReport);
            item.setUrgencyLevel(urgency);
            item.setFeedbackCount(count.intValue());

            entityManager.persist(item);
        });
    }
}