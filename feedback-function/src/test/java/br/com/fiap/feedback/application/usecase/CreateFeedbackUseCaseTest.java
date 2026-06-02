package br.com.fiap.feedback.application.usecase;

import br.com.fiap.feedback.adapter.output.persistence.entity.CourseJpaEntity;
import br.com.fiap.feedback.adapter.output.persistence.entity.FeedbackEntity;
import br.com.fiap.feedback.adapter.output.persistence.entity.StudentEntity;
import br.com.fiap.feedback.adapter.output.persistence.repository.FeedbackJpaRepository;
import br.com.fiap.shared.application.port.output.EnrollmentRepositoryPort;
import br.com.fiap.shared.application.port.output.EventPublisherPort;
import br.com.fiap.shared.domain.entity.Enrollment;
import br.com.fiap.shared.domain.entity.Feedback;
import br.com.fiap.shared.domain.exception.ValidationException;
import br.com.fiap.shared.domain.valueObject.EnrollmentStatus;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateFeedbackUseCaseTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private FeedbackJpaRepository feedbackRepository;

    @Mock
    private EnrollmentRepositoryPort enrollmentRepository;

    @Mock
    private EventPublisherPort eventPublisher;

    private CreateFeedbackUseCase useCase;

    private static final UUID STUDENT_ID = UUID.randomUUID();
    private static final UUID COURSE_ID = UUID.randomUUID();

    private StudentEntity studentEntity;
    private CourseJpaEntity courseEntity;
    private Enrollment activeEnrollment;
    private Enrollment inactiveEnrollment;

    @BeforeEach
    void setUp() {
        useCase = new CreateFeedbackUseCase(entityManager, feedbackRepository, enrollmentRepository,
                eventPublisher);

        studentEntity = new StudentEntity();
        courseEntity = new CourseJpaEntity();
        activeEnrollment = new Enrollment(UUID.randomUUID(), STUDENT_ID, COURSE_ID, LocalDate.now(),
                EnrollmentStatus.ACTIVE);
        inactiveEnrollment = new Enrollment(UUID.randomUUID(), STUDENT_ID, COURSE_ID, LocalDate.now(),
                EnrollmentStatus.INACTIVE);
    }

    @Test
    void deveCriarFeedbackComSucesso() {
        when(entityManager.find(StudentEntity.class, STUDENT_ID)).thenReturn(studentEntity);
        when(entityManager.find(CourseJpaEntity.class, COURSE_ID)).thenReturn(courseEntity);
        when(enrollmentRepository.findByStudentAndCourse(STUDENT_ID, COURSE_ID))
                .thenReturn(Optional.of(activeEnrollment));

        CreateFeedbackCommand command = new CreateFeedbackCommand(STUDENT_ID, COURSE_ID, "Great course", 8);
        Feedback feedback = useCase.execute(command);

        assertNotNull(feedback);
        assertEquals("Great course", feedback.getDescription().valor());
        assertEquals(8, feedback.getScore().valor());
        verify(feedbackRepository).persist(any(FeedbackEntity.class));
        verify(eventPublisher).publish(any());
    }

    @Test
    void deveLancarExcecaoQuandoStudentNaoExiste() {
        when(entityManager.find(StudentEntity.class, STUDENT_ID)).thenReturn(null);

        CreateFeedbackCommand command = new CreateFeedbackCommand(STUDENT_ID, COURSE_ID, "Great course", 8);

        ValidationException ex = assertThrows(ValidationException.class, () -> useCase.execute(command));
        assertEquals("Student not found: " + STUDENT_ID, ex.getMessage());
        verify(feedbackRepository, never()).persist(any(FeedbackEntity.class));
        verify(eventPublisher, never()).publish(any());
    }

    @Test
    void deveLancarExcecaoQuandoCursoNaoExiste() {
        when(entityManager.find(StudentEntity.class, STUDENT_ID)).thenReturn(studentEntity);
        when(entityManager.find(CourseJpaEntity.class, COURSE_ID)).thenReturn(null);

        CreateFeedbackCommand command = new CreateFeedbackCommand(STUDENT_ID, COURSE_ID, "Great course", 8);

        ValidationException ex = assertThrows(ValidationException.class, () -> useCase.execute(command));
        assertEquals("Course not found: " + COURSE_ID, ex.getMessage());
        verify(feedbackRepository, never()).persist(any(FeedbackEntity.class));
        verify(eventPublisher, never()).publish(any());
    }

    @Test
    void deveLancarExcecaoQuandoEnrollmentNaoExiste() {
        when(entityManager.find(StudentEntity.class, STUDENT_ID)).thenReturn(studentEntity);
        when(entityManager.find(CourseJpaEntity.class, COURSE_ID)).thenReturn(courseEntity);
        when(enrollmentRepository.findByStudentAndCourse(STUDENT_ID, COURSE_ID)).thenReturn(Optional.empty());

        CreateFeedbackCommand command = new CreateFeedbackCommand(STUDENT_ID, COURSE_ID, "Great course", 8);

        ValidationException ex = assertThrows(ValidationException.class, () -> useCase.execute(command));
        assertEquals("No enrollment found for student " + STUDENT_ID + " in course " + COURSE_ID, ex.getMessage());
        verify(feedbackRepository, never()).persist(any(FeedbackEntity.class));
        verify(eventPublisher, never()).publish(any());
    }

    @Test
    void deveLancarExcecaoQuandoEnrollmentNaoEstaAtivo() {
        when(entityManager.find(StudentEntity.class, STUDENT_ID)).thenReturn(studentEntity);
        when(entityManager.find(CourseJpaEntity.class, COURSE_ID)).thenReturn(courseEntity);
        when(enrollmentRepository.findByStudentAndCourse(STUDENT_ID, COURSE_ID))
                .thenReturn(Optional.of(inactiveEnrollment));

        CreateFeedbackCommand command = new CreateFeedbackCommand(STUDENT_ID, COURSE_ID, "Great course", 8);

        ValidationException ex = assertThrows(ValidationException.class, () -> useCase.execute(command));
        assertEquals("Enrollment is not active for student " + STUDENT_ID + " in course " + COURSE_ID, ex.getMessage());
        verify(feedbackRepository, never()).persist(any(FeedbackEntity.class));
        verify(eventPublisher, never()).publish(any());
    }

    @Test
    void deveLancarExcecaoQuandoStudentIdNulo() {
        CreateFeedbackCommand command = new CreateFeedbackCommand(null, COURSE_ID, "Great course", 8);

        ValidationException ex = assertThrows(ValidationException.class, () -> useCase.execute(command));
        assertEquals("studentId is required", ex.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoCourseIdNulo() {
        CreateFeedbackCommand command = new CreateFeedbackCommand(STUDENT_ID, null, "Great course", 8);

        ValidationException ex = assertThrows(ValidationException.class, () -> useCase.execute(command));
        assertEquals("courseId is required", ex.getMessage());
    }
}
