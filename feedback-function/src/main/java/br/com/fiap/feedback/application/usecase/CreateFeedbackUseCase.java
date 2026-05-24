package br.com.fiap.feedback.application.usecase;

import br.com.fiap.shared.application.dto.FeedbackRequest;
import br.com.fiap.shared.application.dto.FeedbackResponse;
import br.com.fiap.shared.application.port.output.CourseRepositoryPort;
import br.com.fiap.shared.application.port.output.EnrollmentRepositoryPort;
import br.com.fiap.shared.application.port.output.EventPublisherPort;
import br.com.fiap.shared.application.port.output.FeedbackRepositoryPort;
import br.com.fiap.shared.application.port.output.UserRepositoryPort;
import br.com.fiap.shared.domain.entity.Enrollment;
import br.com.fiap.shared.domain.entity.Feedback;
import br.com.fiap.shared.domain.event.FeedbackCreatedEvent;
import br.com.fiap.shared.domain.exception.ValidationException;
import br.com.fiap.shared.domain.valueObject.EnrollmentStatus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class CreateFeedbackUseCase implements br.com.fiap.shared.application.port.input.CreateFeedbackUseCase {

    @Inject
    UserRepositoryPort userRepository;

    @Inject
    CourseRepositoryPort courseRepository;

    @Inject
    EnrollmentRepositoryPort enrollmentRepository;

    @Inject
    FeedbackRepositoryPort feedbackRepository;

    @Inject
    EventPublisherPort eventPublisher;

    @Override
    public FeedbackResponse execute(FeedbackRequest request) {
        validateBusinessRules(request);

        Feedback feedback = Feedback.create(
                request.studentId(),
                request.courseId(),
                request.description(),
                request.grade()
        );

        feedbackRepository.save(feedback);

        eventPublisher.publish(new FeedbackCreatedEvent(
                feedback.getId(),
                feedback.getDescription().valor(),
                feedback.getScore().valor(),
                feedback.getUrgency().name(),
                feedback.getSubmittedAt()
        ));

        return FeedbackResponse.fromDomain(feedback);
    }

    private void validateBusinessRules(FeedbackRequest request) {
        userRepository.findById(request.studentId())
                .orElseThrow(() -> new ValidationException("Student not found: " + request.studentId()));

        courseRepository.findById(request.courseId())
                .orElseThrow(() -> new ValidationException("Course not found: " + request.courseId()));

        Enrollment enrollment = enrollmentRepository.findByStudentAndCourse(request.studentId(), request.courseId())
                .orElseThrow(() -> new ValidationException(
                        "No enrollment found for student " + request.studentId() + " in course " + request.courseId()));

        if (enrollment.getStatus() != EnrollmentStatus.ACTIVE) {
            throw new ValidationException(
                    "Enrollment is not active for student " + request.studentId() + " in course " + request.courseId());
        }
    }
}
