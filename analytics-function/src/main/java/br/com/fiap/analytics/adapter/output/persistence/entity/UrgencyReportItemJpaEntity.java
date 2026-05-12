package br.com.fiap.analytics.adapter.output.persistence.entity;

import br.com.fiap.shared.domain.valueObject.UrgencyLevel;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "urgency_report_items")
public class UrgencyReportItemJpaEntity {

    @Id
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "weekly_report_id", nullable = false)
    private WeeklyReportJpaEntity weeklyReport;

    @Enumerated
    @Column(name = "urgency_level", nullable = false)
    private UrgencyLevel urgencyLevel;

    @Column(name = "feedback_count", nullable = false)
    private Integer feedbackCount;


    public UrgencyReportItemJpaEntity() {
    }


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public WeeklyReportJpaEntity getWeeklyReport() {
        return weeklyReport;
    }

    public void setWeeklyReport(WeeklyReportJpaEntity weeklyReport) {
        this.weeklyReport = weeklyReport;
    }

    public UrgencyLevel getUrgencyLevel() {
        return urgencyLevel;
    }

    public void setUrgencyLevel(UrgencyLevel urgencyLevel) {
        this.urgencyLevel = urgencyLevel;
    }

    public Integer getFeedbackCount() {
        return feedbackCount;
    }

    public void setFeedbackCount(Integer feedbackCount) {
        this.feedbackCount = feedbackCount;
    }
}
