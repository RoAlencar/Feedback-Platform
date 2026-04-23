package br.com.fiap.analytics.adapter.output.persistence.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "daily_report_items")
public class DailyReportItemJpaEntity {

    @Id
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "weekly_report_id", nullable = false)
    private WeeklyReportJpaEntity weeklyReport;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "feedback_count", nullable = false)
    private Integer feedbackCount;


    public DailyReportItemJpaEntity() {
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getFeedbackCount() {
        return feedbackCount;
    }

    public void setFeedbackCount(Integer feedbackCount) {
        this.feedbackCount = feedbackCount;
    }
}
