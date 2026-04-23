package br.com.fiap.feedback.adapter.output.persistence.entity;

import br.com.fiap.shared.domain.entity.Feedback;
import br.com.fiap.shared.domain.valueObject.Description;
import br.com.fiap.shared.domain.valueObject.Score;
import br.com.fiap.shared.domain.valueObject.Urgency;
import br.com.fiap.shared.domain.valueObject.ProcessStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "feedbacks")
public class FeedbackEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private int score;

    @Enumerated(EnumType.STRING)
    @Column(name = "urgency_level", nullable = false)
    private Urgency urgency;

    @Enumerated(EnumType.STRING)
    @Column(name = "process_status", nullable = false)
    private ProcessStatus processStatus;

    @Column(name = "submitted_at", nullable = false)
    private LocalDateTime submittedAt;

    public FeedbackEntity() {}

    public static FeedbackEntity fromDomain(Feedback feedback) {
        FeedbackEntity entity = new FeedbackEntity();
        entity.id = feedback.getId();
        entity.description = feedback.getDescription().valor();
        entity.score = feedback.getScore().valor();
        entity.urgency = feedback.getUrgency();
        entity.processStatus = feedback.getProcessStatus();
        entity.submittedAt = feedback.getSubmittedAt();
        return entity;
    }

    public Feedback toDomain() {
        return new Feedback(
                id,
                new Description(description),
                new Score(score),
                processStatus,
                submittedAt
        );
    }
}
