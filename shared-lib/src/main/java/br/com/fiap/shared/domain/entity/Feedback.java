package br.com.fiap.shared.domain.entity;

import br.com.fiap.shared.common.DateUtils;
import br.com.fiap.shared.domain.valueObject.Description;
import br.com.fiap.shared.domain.valueObject.Grade;
import br.com.fiap.shared.domain.valueObject.UrgencyLevel;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public final class Feedback {

    private final UUID id;
    private final Description description;
    private final Grade grade;
    private final UrgencyLevel urgency;
    private final LocalDateTime createdAt;

    public Feedback(UUID id, Description description, Grade grade, LocalDateTime createdAt) {
        this.id = Objects.requireNonNull(id,"id");
        this.description = Objects.requireNonNull(description, "description");
        this.grade = Objects.requireNonNull(grade, "grade");
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt");
        this.urgency = UrgencyLevel.fromGrade(grade);
    }

    public static Feedback create(String description, int grade) {
        return new Feedback(
                UUID.randomUUID(),
                new Description(description),
                new Grade(grade),
                DateUtils.now());
    }

    public UUID getId() {
        return id;
    }

    public Description getDescription() {
        return description;
    }

    public Grade getGrade() {
        return grade;
    }

    public UrgencyLevel getUrgency() {
        return urgency;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
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
