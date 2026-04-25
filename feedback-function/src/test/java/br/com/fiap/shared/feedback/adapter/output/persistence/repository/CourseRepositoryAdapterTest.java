package br.com.fiap.shared.feedback.adapter.output.persistence.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import br.com.fiap.shared.domain.entity.Course;
import br.com.fiap.shared.feedback.adapter.output.persistence.entity.CourseJpaEntity;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@QuarkusTest
class CourseRepositoryAdapterTest {

    @Inject
    CourseRepositoryAdapter courseRepository;

    @Inject
    EntityManager entityManager;

    @Test
    @Transactional
    void shouldPersistCourseSuccessfully() {
        Course course = Course.create("Java Programming");

        courseRepository.save(course);

        CourseJpaEntity persisted = entityManager.find(CourseJpaEntity.class, course.getId());

        assertNotNull(persisted);
        assertEquals(course.getId(), persisted.getId());
        assertEquals("Java Programming", persisted.getName());
    }

    @Test
    @Transactional
    void shouldFindCourseById() {
        Course course = Course.create("Spring Boot Fundamentals");
        courseRepository.save(course);

        Optional<Course> found = courseRepository.findById(course.getId());

        assertTrue(found.isPresent());
        assertEquals(course.getId(), found.get().getId());
        assertEquals("Spring Boot Fundamentals", found.get().getName().value());
    }

    @Test
    @Transactional
    void shouldReturnEmptyWhenCourseNotFound() {
        UUID nonExistentId = UUID.randomUUID();

        Optional<Course> found = courseRepository.findById(nonExistentId);

        assertFalse(found.isPresent());
    }

    @Test
    @Transactional
    void shouldFindAllCourses() {
        Course course1 = Course.create("Microservices Architecture");
        Course course2 = Course.create("Clean Code Principles");
        
        courseRepository.save(course1);
        courseRepository.save(course2);

        List<Course> courses = courseRepository.findAll();

        assertTrue(courses.size() >= 2);
        assertTrue(courses.stream().anyMatch(c -> c.getId().equals(course1.getId())));
        assertTrue(courses.stream().anyMatch(c -> c.getId().equals(course2.getId())));
    }

    @Test
    @Transactional
    void shouldPreserveCourseName() {
        String courseName = "Domain-Driven Design";
        Course course = Course.create(courseName);

        courseRepository.save(course);

        Optional<Course> found = courseRepository.findById(course.getId());

        assertTrue(found.isPresent());
        assertEquals(courseName, found.get().getName().value());
    }
}
