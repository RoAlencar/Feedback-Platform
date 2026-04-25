package br.com.fiap.shared.domain.entity;

import br.com.fiap.shared.common.DateUtils;
import br.com.fiap.shared.domain.valueObject.Description;
import br.com.fiap.shared.domain.valueObject.Score;
import br.com.fiap.shared.domain.valueObject.ProcessStatus;
import br.com.fiap.shared.domain.valueObject.UrgencyLevel;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public final class Feedback {

    private final UUID id;
    private final Description description;
    private final Score score;
    private final UrgencyLevel urgency;
    private final LocalDateTime submittedAt;
    private final ProcessStatus status;

    public Feedback(UUID id, Description description, Score score,ProcessStatus processStatus, LocalDateTime submittedAt) {
        this.id = Objects.requireNonNull(id,"id");
        this.description = Objects.requireNonNull(description, "description");
        this.score = Objects.requireNonNull(score, "score");
        this.submittedAt = Objects.requireNonNull(submittedAt, "submittedAt");
        this.urgency = UrgencyLevel.fromGrade(score);
        this.status = Objects.requireNonNull(processStatus, "processStatus");
    }

    public static Feedback create(String description, int grade, ProcessStatus processStatus) {
        return new Feedback(
                UUID.randomUUID(),
                new Description(description),
                new Score(grade),
                processStatus,
                DateUtils.now());
    }

    public UUID getId() {
        return id;
    }

    public Description getDescription() {
        return description;
    }

    public Score getScore() {
        return score;
    }

    public UrgencyLevel getUrgency() {
        return urgency;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public ProcessStatus getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Feedback other)) return false;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}
