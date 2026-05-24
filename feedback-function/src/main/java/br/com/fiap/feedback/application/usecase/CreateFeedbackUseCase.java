package br.com.fiap.feedback.application.usecase;

import br.com.fiap.feedback.adapter.output.persistence.entity.CourseJpaEntity;
import br.com.fiap.feedback.adapter.output.persistence.entity.FeedbackEntity;
import br.com.fiap.feedback.adapter.output.persistence.entity.StudentEntity;
import br.com.fiap.feedback.adapter.output.persistence.mapper.FeedbackMapper;
import br.com.fiap.feedback.adapter.output.persistence.repository.FeedbackJpaRepository;
import br.com.fiap.feedback.application.service.NotificationService;
import br.com.fiap.shared.application.port.output.EnrollmentRepositoryPort;
import br.com.fiap.shared.domain.entity.Enrollment;
import br.com.fiap.shared.domain.entity.Feedback;
import br.com.fiap.shared.domain.exception.ValidationException;
import br.com.fiap.shared.domain.valueObject.EnrollmentStatus;
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

        validateBusinessRules(command);

        Feedback feedback = Feedback.create(
                command.studentId(),
                command.courseId(),
                command.description(),
                command.score());

        FeedbackEntity entity = FeedbackMapper.toJpaEntity(feedback, entityManager);
        feedbackRepository.persist(entity);

        return feedback;
    }

    private void validateBusinessRules(CreateFeedbackCommand command) {

        if (command.studentId() == null) {
            throw new ValidationException("studentId is required");
        }

        if (command.courseId() == null) {
            throw new ValidationException("courseId is required");
        }

        StudentEntity student = entityManager.find(StudentEntity.class, command.studentId());
        if (student == null) {
            throw new ValidationException("Student not found: " + command.studentId());
        }

        CourseJpaEntity course = entityManager.find(CourseJpaEntity.class, command.courseId());
        if (course == null) {
            throw new ValidationException("Course not found: " + command.courseId());
        }

        Enrollment enrollment = enrollmentRepository
                .findByStudentAndCourse(command.studentId(), command.courseId())
                .orElseThrow(() -> new ValidationException(
                        "No enrollment found for student " + command.studentId() + " in course " + command.courseId()));

        if (enrollment.getStatus() != EnrollmentStatus.ACTIVE) {
            throw new ValidationException(
                    "Enrollment is not active for student " + command.studentId() + " in course " + command.courseId());
        }
    }
}
