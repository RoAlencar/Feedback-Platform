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
    private final UUID studentId;
    private final UUID courseId;
    private final Description description;
    private final Score score;
    private final UrgencyLevel urgency;
    private final LocalDateTime submittedAt;
    private final ProcessStatus status;

    public Feedback(UUID id, UUID studentId, UUID courseId, Description description, Score score, ProcessStatus processStatus, LocalDateTime submittedAt) {
        this.id = Objects.requireNonNull(id,"id");
        this.studentId = Objects.requireNonNull(studentId, "studentId");
        this.courseId = Objects.requireNonNull(courseId, "courseId");
        this.description = Objects.requireNonNull(description, "description");
        this.score = Objects.requireNonNull(score, "score");
        this.submittedAt = Objects.requireNonNull(submittedAt, "submittedAt");
        this.urgency = UrgencyLevel.fromGrade(score);
        this.status = Objects.requireNonNull(processStatus, "processStatus");
    }

    public static Feedback create(UUID studentId, UUID courseId, String description, int grade) {
        return new Feedback(
                UUID.randomUUID(),
                studentId,
                courseId,
                new Description(description),
                new Score(grade),
                ProcessStatus.PENDING,
                DateUtils.now());
    }

    public UUID getId() {
        return id;
    }

    public UUID getStudentId() {
        return studentId;
    }

    public UUID getCourseId() {
        return courseId;
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
