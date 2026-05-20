package br.com.fiap.feedback.application.usecase;

import br.com.fiap.feedback.adapter.output.persistence.entity.CourseJpaEntity;
import br.com.fiap.feedback.adapter.output.persistence.entity.FeedbackEntity;
import br.com.fiap.feedback.adapter.output.persistence.entity.StudentEntity;
import br.com.fiap.feedback.adapter.output.persistence.mapper.FeedbackMapper;
import br.com.fiap.feedback.adapter.output.persistence.repository.FeedbackJpaRepository;
import br.com.fiap.feedback.application.usecase.exceptions.CourseFoundException;
import br.com.fiap.feedback.application.usecase.exceptions.EnrollmentNotFoundException;
import br.com.fiap.feedback.application.usecase.exceptions.InvalidFeedbackException;
import br.com.fiap.feedback.application.usecase.exceptions.StudentNotFoundException;
import br.com.fiap.feedback.application.service.NotificationService;
import br.com.fiap.shared.application.port.output.EnrollmentRepositoryPort;
import br.com.fiap.shared.domain.entity.Enrollment;
import br.com.fiap.shared.domain.entity.Feedback;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;

@ApplicationScoped
public class CreateFeedbackUseCase {

    private static final Logger LOG = Logger.getLogger(CreateFeedbackUseCase.class);

    private final NotificationService notificationService;
    private final EntityManager entityManager;
    private final FeedbackJpaRepository feedbackRepository;
    private final EnrollmentRepositoryPort enrollmentRepository;

    public CreateFeedbackUseCase(EntityManager entityManager, FeedbackJpaRepository feedbackRepository,
            EnrollmentRepositoryPort enrollmentRepository, NotificationService notificationService) {
        this.entityManager = entityManager;
        this.feedbackRepository = feedbackRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.notificationService = notificationService;
    }

    @Transactional
    public Feedback execute(CreateFeedbackCommand command) {

        LOG.info("Starting feedback processing");

        validatePayload(command);

        StudentEntity student = entityManager.find(StudentEntity.class, command.studentId());
        if (student == null) {
            throw new StudentNotFoundException("Student not found");
        }

        CourseJpaEntity course = entityManager.find(CourseJpaEntity.class, command.courseId());

        if (course == null) {
            throw new CourseFoundException("Course not found");
        }

        Enrollment enrollment = enrollmentRepository
                .findByStudentAndCourse(command.studentId(), command.courseId())
                .orElseThrow(() -> new EnrollmentNotFoundException(
                        "Enrollment not found for the student and course provideds"));

        if (!"ACTIVE".equals(enrollment.getStatus().name())) {
            throw new IllegalArgumentException("Student enrollment is not active");
        }

        Feedback feedback = Feedback.create(
                command.studentId(),
                command.courseId(),
                command.description(),
                command.score());

        FeedbackEntity entity = FeedbackMapper.toJpaEntity(feedback, entityManager);
        feedbackRepository.persist(entity);

        return feedback;
    }

    private void validatePayload(CreateFeedbackCommand command) {

        if (command.studentId() == null) {
            throw new InvalidFeedbackException("studentId is required");
        }

        if (command.courseId() == null) {
            throw new InvalidFeedbackException("courseId is required");
        }

        if (command.description() == null || command.description().isBlank()) {
            throw new InvalidFeedbackException("Description is required");
        }

        if (command.score() == null) {
            throw new InvalidFeedbackException("Score is is required");
        }

        if (command.score() < 0 || command.score() > 10) {
            throw new InvalidFeedbackException("Score should be between 0-10");
        }
    }
}
