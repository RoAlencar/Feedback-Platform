package br.com.fiap.shared.application.port.output;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.com.fiap.shared.domain.entity.Course;

public interface CourseRepositoryPort {

    void save(Course course);

    Optional<Course> findById(UUID id);

    List<Course> findAll();
}
