package br.com.fiap.feedback.adapter.output.persistence.mapper;

import br.com.fiap.feedback.adapter.output.persistence.entity.StudentJpaEntity;
import br.com.fiap.shared.domain.entity.Student;
import br.com.fiap.shared.domain.valueObject.Email;
import br.com.fiap.shared.domain.valueObject.Name;

public final class StudentMapper {

    private StudentMapper() {
    }

    public static StudentJpaEntity toJpaEntity(Student student) {
        return new StudentJpaEntity(
                student.getId(),
                student.getName().value(),
                student.getEmail().value(),
                student.isActive()
        );
    }

    public static Student toDomain(StudentJpaEntity entity) {
        return new Student(
                entity.getId(),
                new Name(entity.getName()),
                new Email(entity.getEmail()),
                entity.isActive()
        );
    }
}