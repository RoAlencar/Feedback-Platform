package br.com.fiap.feedback.adapter.output.persistence.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import br.com.fiap.shared.application.port.output.CourseRepositoryPort;
import br.com.fiap.shared.application.port.output.EnrollmentRepositoryPort;
import br.com.fiap.shared.application.port.output.UserRepositoryPort;
import br.com.fiap.shared.domain.entity.Course;
import br.com.fiap.shared.domain.entity.Enrollment;
import br.com.fiap.shared.domain.entity.Student;
import br.com.fiap.shared.domain.valueObject.EnrollmentStatus;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@QuarkusTest
@QuarkusTestResource(PostgresTestResource.class)
class EnrollmentRepositoryAdapterTest {

    @Inject
    UserRepositoryPort studentRepository;

    @Inject
    EnrollmentRepositoryPort enrollmentRepository;

    @Inject
    CourseRepositoryPort courseRepository;

    @Test
    @Transactional
    void shouldPersistEnrollmentSuccessfully() {

        // Arrange
        Course course = Course.create("Test Course");
        courseRepository.save(course);

        Student student = Student.create(
                "João Teste",
                "joao" + UUID.randomUUID() + "@email.com");

        studentRepository.save(student);

        Enrollment enrollment = Enrollment.create(
                student.getId(),
                course.getId());

        // Act
        enrollmentRepository.save(enrollment);

        // Assert
        Optional<Enrollment> persisted = enrollmentRepository.findById(enrollment.getId());

        assertTrue(persisted.isPresent());
        assertEquals(enrollment.getId(), persisted.get().getId());
        assertEquals(student.getId(), persisted.get().getStudentId());
        assertEquals(course.getId(), persisted.get().getCourseId());
        assertEquals(LocalDate.now(), persisted.get().getEnrollmentDate());
        assertEquals(EnrollmentStatus.ACTIVE, persisted.get().getStatus());
    }

    @Test
    @Transactional
    void shouldFindEnrollmentById() {

        // Arrange
        Course course = Course.create("Advanced Java");
        courseRepository.save(course);

        Student student = Student.create(
                "Maria Teste",
                "maria" + UUID.randomUUID() + "@email.com");

        studentRepository.save(student);

        Enrollment enrollment = Enrollment.create(
                student.getId(),
                course.getId());

        enrollmentRepository.save(enrollment);

        // Act
        Optional<Enrollment> found = enrollmentRepository.findById(enrollment.getId());

        // Assert
        assertTrue(found.isPresent());
        assertEquals(enrollment.getId(), found.get().getId());
        assertEquals(student.getId(), found.get().getStudentId());
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
        Student student = Student.create(
                "Carlos Teste",
                "carlos" + UUID.randomUUID() + "@email.com");

        studentRepository.save(student);

        Course course1 = Course.create("Spring Boot");
        Course course2 = Course.create("Microservices");

        courseRepository.save(course1);
        courseRepository.save(course2);

        Enrollment enrollment1 = Enrollment.create(
                student.getId(),
                course1.getId());

        Enrollment enrollment2 = Enrollment.create(
                student.getId(),
                course2.getId());

        enrollmentRepository.save(enrollment1);
        enrollmentRepository.save(enrollment2);

        // Act
        List<Enrollment> enrollments = enrollmentRepository.findByStudentId(student.getId());

        // Assert
        assertTrue(enrollments.size() >= 2);

        assertTrue(
                enrollments.stream()
                        .allMatch(e -> e.getStudentId().equals(student.getId())));
    }

    @Test
    @Transactional
    void shouldFindEnrollmentsByCourseId() {

        // Arrange
        Course course = Course.create("Database Design");
        courseRepository.save(course);

        Student student1 = Student.create(
                "Aluno 1",
                "aluno1" + UUID.randomUUID() + "@email.com");

        Student student2 = Student.create(
                "Aluno 2",
                "aluno2" + UUID.randomUUID() + "@email.com");

        studentRepository.save(student1);
        studentRepository.save(student2);

        Enrollment enrollment1 = Enrollment.create(
                student1.getId(),
                course.getId());

        Enrollment enrollment2 = Enrollment.create(
                student2.getId(),
                course.getId());

        enrollmentRepository.save(enrollment1);
        enrollmentRepository.save(enrollment2);

        // Act
        List<Enrollment> enrollments = enrollmentRepository.findByCourseId(course.getId());

        // Assert
        assertTrue(enrollments.size() >= 2);

        assertTrue(
                enrollments.stream()
                        .allMatch(e -> e.getCourseId().equals(course.getId())));
    }

    @Test
    @Transactional
    void shouldFindEnrollmentByStudentAndCourse() {

        // Arrange
        Course course = Course.create("Software Architecture");
        courseRepository.save(course);

        Student student = Student.create(
                "Fernanda Teste",
                "fernanda" + UUID.randomUUID() + "@email.com");

        studentRepository.save(student);

        Enrollment enrollment = Enrollment.create(
                student.getId(),
                course.getId());

        enrollmentRepository.save(enrollment);

        // Act
        Optional<Enrollment> found = enrollmentRepository.findByStudentAndCourse(
                student.getId(),
                course.getId());

        // Assert
        assertTrue(found.isPresent());
        assertEquals(student.getId(), found.get().getStudentId());
        assertEquals(course.getId(), found.get().getCourseId());
    }

    @Test
    @Transactional
    void shouldPersistEnrollmentWithInactiveStatus() {

        // Arrange
        Course course = Course.create("Inactive Course Test");
        courseRepository.save(course);

        Student student = Student.create(
                "Inactive Student",
                "inactive" + UUID.randomUUID() + "@email.com");

        studentRepository.save(student);

        Enrollment enrollment = new Enrollment(
                UUID.randomUUID(),
                student.getId(),
                course.getId(),
                LocalDate.now().minusDays(30),
                EnrollmentStatus.INACTIVE);

        // Act
        enrollmentRepository.save(enrollment);

        // Assert
        Optional<Enrollment> persisted = enrollmentRepository.findById(enrollment.getId());

        assertTrue(persisted.isPresent());
        assertEquals(EnrollmentStatus.INACTIVE, persisted.get().getStatus());
    }

    @Test
    @Transactional
    void shouldPreserveEnrollmentDate() {

        // Arrange
        Course course = Course.create("Date Preservation Test");
        courseRepository.save(course);

        Student student = Student.create(
                "Date Student",
                "date" + UUID.randomUUID() + "@email.com");

        studentRepository.save(student);

        LocalDate enrollmentDate = LocalDate.now().minusDays(10);

        Enrollment enrollment = new Enrollment(
                UUID.randomUUID(),
                student.getId(),
                course.getId(),
                enrollmentDate,
                EnrollmentStatus.ACTIVE);

        // Act
        enrollmentRepository.save(enrollment);

        // Assert
        Optional<Enrollment> found = enrollmentRepository.findById(enrollment.getId());

        assertTrue(found.isPresent());
        assertEquals(enrollmentDate, found.get().getEnrollmentDate());
    }
}