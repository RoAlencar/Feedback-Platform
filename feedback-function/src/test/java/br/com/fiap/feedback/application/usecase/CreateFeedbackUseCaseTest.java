package br.com.fiap.feedback.application.usecase;

import br.com.fiap.shared.application.dto.FeedbackRequest;
import br.com.fiap.shared.application.dto.FeedbackResponse;
import br.com.fiap.shared.application.port.output.CourseRepositoryPort;
import br.com.fiap.shared.application.port.output.EnrollmentRepositoryPort;
import br.com.fiap.shared.application.port.output.EventPublisherPort;
import br.com.fiap.shared.application.port.output.FeedbackRepositoryPort;
import br.com.fiap.shared.application.port.output.UserRepositoryPort;
import br.com.fiap.shared.domain.entity.Course;
import br.com.fiap.shared.domain.entity.Enrollment;
import br.com.fiap.shared.domain.entity.Student;
import br.com.fiap.shared.domain.exception.ValidationException;
import br.com.fiap.shared.domain.valueObject.EnrollmentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
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
    private UserRepositoryPort userRepository;

    @Mock
    private CourseRepositoryPort courseRepository;

    @Mock
    private EnrollmentRepositoryPort enrollmentRepository;

    @Mock
    private FeedbackRepositoryPort feedbackRepository;

    @Mock
    private EventPublisherPort eventPublisher;

    @InjectMocks
    private CreateFeedbackUseCase useCase;

    private static final UUID STUDENT_ID = UUID.randomUUID();
    private static final UUID COURSE_ID = UUID.randomUUID();

    private Student student;
    private Course course;
    private Enrollment activeEnrollment;
    private Enrollment inactiveEnrollment;

    @BeforeEach
    void setUp() {
        student = Student.create("John Doe", "john@example.com");
        course = Course.create("Java 21");
        activeEnrollment = new Enrollment(UUID.randomUUID(), STUDENT_ID, COURSE_ID, LocalDate.now(), EnrollmentStatus.ACTIVE);
        inactiveEnrollment = new Enrollment(UUID.randomUUID(), STUDENT_ID, COURSE_ID, LocalDate.now(), EnrollmentStatus.INACTIVE);
    }

    @Test
    void devecriarFeedbackComSucesso() {
        when(userRepository.findById(STUDENT_ID)).thenReturn(Optional.of(student));
        when(courseRepository.findById(COURSE_ID)).thenReturn(Optional.of(course));
        when(enrollmentRepository.findByStudentAndCourse(STUDENT_ID, COURSE_ID)).thenReturn(Optional.of(activeEnrollment));

        FeedbackRequest request = new FeedbackRequest(STUDENT_ID, COURSE_ID, "Great course", 8);
        FeedbackResponse response = useCase.execute(request);

        assertNotNull(response);
        assertEquals("Great course", response.description());
        assertEquals(8, response.score());
        verify(feedbackRepository).save(any());
        verify(eventPublisher).publish(any());
    }

    @Test
    void deveLancarExcecaoQuandoStudentNaoExiste() {
        when(userRepository.findById(STUDENT_ID)).thenReturn(Optional.empty());

        FeedbackRequest request = new FeedbackRequest(STUDENT_ID, COURSE_ID, "Great course", 8);

        ValidationException ex = assertThrows(ValidationException.class, () -> useCase.execute(request));
        assertEquals("Student not found: " + STUDENT_ID, ex.getMessage());
        verify(feedbackRepository, never()).save(any());
        verify(eventPublisher, never()).publish(any());
    }

    @Test
    void deveLancarExcecaoQuandoCursoNaoExiste() {
        when(userRepository.findById(STUDENT_ID)).thenReturn(Optional.of(student));
        when(courseRepository.findById(COURSE_ID)).thenReturn(Optional.empty());

        FeedbackRequest request = new FeedbackRequest(STUDENT_ID, COURSE_ID, "Great course", 8);

        ValidationException ex = assertThrows(ValidationException.class, () -> useCase.execute(request));
        assertEquals("Course not found: " + COURSE_ID, ex.getMessage());
        verify(feedbackRepository, never()).save(any());
        verify(eventPublisher, never()).publish(any());
    }

    @Test
    void deveLancarExcecaoQuandoEnrollmentNaoExiste() {
        when(userRepository.findById(STUDENT_ID)).thenReturn(Optional.of(student));
        when(courseRepository.findById(COURSE_ID)).thenReturn(Optional.of(course));
        when(enrollmentRepository.findByStudentAndCourse(STUDENT_ID, COURSE_ID)).thenReturn(Optional.empty());

        FeedbackRequest request = new FeedbackRequest(STUDENT_ID, COURSE_ID, "Great course", 8);

        ValidationException ex = assertThrows(ValidationException.class, () -> useCase.execute(request));
        assertEquals("No enrollment found for student " + STUDENT_ID + " in course " + COURSE_ID, ex.getMessage());
        verify(feedbackRepository, never()).save(any());
        verify(eventPublisher, never()).publish(any());
    }

    @Test
    void deveLancarExcecaoQuandoEnrollmentNaoEstaAtivo() {
        when(userRepository.findById(STUDENT_ID)).thenReturn(Optional.of(student));
        when(courseRepository.findById(COURSE_ID)).thenReturn(Optional.of(course));
        when(enrollmentRepository.findByStudentAndCourse(STUDENT_ID, COURSE_ID)).thenReturn(Optional.of(inactiveEnrollment));

        FeedbackRequest request = new FeedbackRequest(STUDENT_ID, COURSE_ID, "Great course", 8);

        ValidationException ex = assertThrows(ValidationException.class, () -> useCase.execute(request));
        assertEquals("Enrollment is not active for student " + STUDENT_ID + " in course " + COURSE_ID, ex.getMessage());
        verify(feedbackRepository, never()).save(any());
        verify(eventPublisher, never()).publish(any());
    }
}
