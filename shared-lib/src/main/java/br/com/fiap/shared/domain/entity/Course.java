package br.com.fiap.shared.domain.entity;

import java.util.Objects;
import java.util.UUID;

import br.com.fiap.shared.domain.valueObject.CourseName;

public final class Course {

    private final UUID id;
    private final CourseName name;

    public Course(UUID id, CourseName name) {
        this.id = Objects.requireNonNull(id, "id is required");
        this.name = Objects.requireNonNull(name, "name is required");
    }

    public static Course create(String name) {
        return new Course(UUID.randomUUID(), new CourseName(name));
    }

    public UUID getId() {
        return id;
    }

    public CourseName getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Course other)) return false;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
