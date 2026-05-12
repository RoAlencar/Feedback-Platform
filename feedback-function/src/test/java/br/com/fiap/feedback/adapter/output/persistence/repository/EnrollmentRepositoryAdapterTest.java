package br.com.fiap.feedback.adapter.output.persistence.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import br.com.fiap.shared.domain.entity.Course;
import br.com.fiap.shared.domain.entity.Enrollment;
import br.com.fiap.shared.domain.valueObject.EnrollmentStatus;
import br.com.fiap.feedback.adapter.output.persistence.entity.EnrollmentJpaEntity;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@QuarkusTest
class EnrollmentRepositoryAdapterTest {

    @Inject
    EnrollmentRepositoryAdapter enrollmentRepository;

    @Inject
    CourseRepositoryAdapter courseRepository;

    @Inject
    EntityManager entityManager;

    @Test
    @Transactional
    void shouldPersistEnrollmentSuccessfully() {
        // Arrange
        Course course = Course.create("Test Course");
        courseRepository.save(course);
        
        UUID studentId = UUID.randomUUID();
        Enrollment enrollment = Enrollment.create(studentId, course.getId());

        // Act
        enrollmentRepository.save(enrollment);

        // Assert
        EnrollmentJpaEntity persisted = entityManager.find(EnrollmentJpaEntity.class, enrollment.getId());

        assertNotNull(persisted);
        assertEquals(enrollment.getId(), persisted.getId());
        assertEquals(studentId, persisted.getStudentId());
        assertEquals(course.getId(), persisted.getCourseId());
        assertEquals(LocalDate.now(), persisted.getEnrollmentDate());
        assertEquals(EnrollmentStatus.ACTIVE, persisted.getStatus());
    }

    @Test
    @Transactional
    void shouldFindEnrollmentById() {
        // Arrange
        Course course = Course.create("Advanced Java");
        courseRepository.save(course);
        
        UUID studentId = UUID.randomUUID();
        Enrollment enrollment = Enrollment.create(studentId, course.getId());
        enrollmentRepository.save(enrollment);

        // Act
        Optional<Enrollment> found = enrollmentRepository.findById(enrollment.getId());

        // Assert
        assertTrue(found.isPresent());
        assertEquals(enrollment.getId(), found.get().getId());
        assertEquals(studentId, found.get().getStudentId());
        assertEquals(course.getId(), found.get().getCourseId());
    }

    @Test
    @Transactional
    void shouldReturnEmptyWhenEnrollmentNotFound() {
        UUID nonExistentId = UUID.randomUUID();

        Optional<Enrollment> found = enrollmentRepository.findById(nonExistentId);

        assertFalse(found.isPresent());
    }

    @Test
    @Transactional
    void shouldFindEnrollmentsByStudentId() {
        // Arrange
        UUID studentId = UUID.randomUUID();
        
        Course course1 = Course.create("Spring Boot");
        Course course2 = Course.create("Microservices");
        courseRepository.save(course1);
        courseRepository.save(course2);
        
        Enrollment enrollment1 = Enrollment.create(studentId, course1.getId());
        Enrollment enrollment2 = Enrollment.create(studentId, course2.getId());
        enrollmentRepository.save(enrollment1);
        enrollmentRepository.save(enrollment2);

        // Act
        List<Enrollment> enrollments = enrollmentRepository.findByStudentId(studentId);

        // Assert
        assertTrue(enrollments.size() >= 2);
        assertTrue(enrollments.stream().allMatch(e -> e.getStudentId().equals(studentId)));
    }

    @Test
    @Transactional
    void shouldFindEnrollmentsByCourseId() {
        // Arrange
        Course course = Course.create("Database Design");
        courseRepository.save(course);
        
        UUID student1Id = UUID.randomUUID();
        UUID student2Id = UUID.randomUUID();
        
        Enrollment enrollment1 = Enrollment.create(student1Id, course.getId());
        Enrollment enrollment2 = Enrollment.create(student2Id, course.getId());
        enrollmentRepository.save(enrollment1);
        enrollmentRepository.save(enrollment2);

        // Act
        List<Enrollment> enrollments = enrollmentRepository.findByCourseId(course.getId());

        // Assert
        assertTrue(enrollments.size() >= 2);
        assertTrue(enrollments.stream().allMatch(e -> e.getCourseId().equals(course.getId())));
    }

    @Test
    @Transactional
    void shouldFindEnrollmentByStudentAndCourse() {
        // Arrange
        Course course = Course.create("Software Architecture");
        courseRepository.save(course);
        
        UUID studentId = UUID.randomUUID();
        Enrollment enrollment = Enrollment.create(studentId, course.getId());
        enrollmentRepository.save(enrollment);

        // Act
        Optional<Enrollment> found = enrollmentRepository.findByStudentAndCourse(studentId, course.getId());

        // Assert
        assertTrue(found.isPresent());
        assertEquals(studentId, found.get().getStudentId());
        assertEquals(course.getId(), found.get().getCourseId());
    }

    @Test
    @Transactional
    void shouldPersistEnrollmentWithInactiveStatus() {
        // Arrange
        Course course = Course.create("Inactive Course Test");
        courseRepository.save(course);
        
        UUID studentId = UUID.randomUUID();
        Enrollment enrollment = new Enrollment(
                UUID.randomUUID(),
                studentId,
                course.getId(),
                LocalDate.now().minusDays(30),
                EnrollmentStatus.INACTIVE
        );

        // Act
        enrollmentRepository.save(enrollment);

        // Assert
        EnrollmentJpaEntity persisted = entityManager.find(EnrollmentJpaEntity.class, enrollment.getId());

        assertNotNull(persisted);
        assertEquals(EnrollmentStatus.INACTIVE, persisted.getStatus());
    }

    @Test
    @Transactional
    void shouldPreserveEnrollmentDate() {
        // Arrange
        Course course = Course.create("Date Preservation Test");
        courseRepository.save(course);
        
        UUID studentId = UUID.randomUUID();
        LocalDate enrollmentDate = LocalDate.now().minusDays(10);
        
        Enrollment enrollment = new Enrollment(
                UUID.randomUUID(),
                studentId,
                course.getId(),
                enrollmentDate,
                EnrollmentStatus.ACTIVE
        );

        // Act
        enrollmentRepository.save(enrollment);

        // Assert
        Optional<Enrollment> found = enrollmentRepository.findById(enrollment.getId());

        assertTrue(found.isPresent());
        assertEquals(enrollmentDate, found.get().getEnrollmentDate());
    }
}
