package br.com.fiap.feedback.adapter.output.persistence.entity;

import br.com.fiap.shared.domain.entity.Student;
import br.com.fiap.shared.domain.valueObject.Name;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "students")
public class StudentEntity {

    @Id
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;


    public static StudentEntity fromDomain(Student student) {
        StudentEntity entity = new StudentEntity();
        entity.id = student.getId();
        entity.name = student.getName().value();
        entity.email = student.getEmail().value();
        entity.isActive = student.isActive();
        return entity;
    }

    public Student toDomain() {
        return new Student(
                this.id,
                new Name(this.name),
                new br.com.fiap.shared.domain.valueObject.Email(this.email),
                this.isActive
        );
    }

}
