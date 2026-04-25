package br.com.fiap.shared.feedback.adapter.output.persistence.mapper;

import br.com.fiap.shared.domain.entity.Course;
import br.com.fiap.shared.domain.valueObject.CourseName;
import br.com.fiap.shared.feedback.adapter.output.persistence.entity.CourseJpaEntity;

public class CourseMapper {

    public static CourseJpaEntity toJpaEntity(Course domain) {
        CourseJpaEntity entity = new CourseJpaEntity();
        entity.setId(domain.getId());
        entity.setName(domain.getName().value());
        return entity;
    }

    public static Course toDomain(CourseJpaEntity entity) {
        return new Course(
                entity.getId(),
                new CourseName(entity.getName())
        );
    }
}
